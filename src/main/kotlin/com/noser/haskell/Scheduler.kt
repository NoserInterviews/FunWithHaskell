package com.noser.haskell

import java.time.Instant
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

typealias Period = Long
typealias Phase = Long

class Scheduler(private val name: String) {

    init {
        Executors
            .newSingleThreadScheduledExecutor {
                Thread(it, "$name-Thread").apply { isDaemon = true }
            }
            .scheduleAtFixedRate({ executeTasks(Instant.now().epochSecond) }, 2L, 1L, TimeUnit.SECONDS)
    }

    @Volatile
    private var tasks: Seq<Task> = Seq.empty()

    @Synchronized
    fun addTask(id: String, period: Period, runnable: () -> Unit): Maybe<Task> {

        val taskMaybe = when {
            period < 1L               -> Maybe.nothing()
            tasks.any { it.id == id } -> Maybe.nothing()
            else                      -> Maybe.just(findBestPhase(tasks, period))
        }
            .map { Task(id, period, it, runnable) }

        taskMaybe.forEach { tasks = tasks.prepend(it) }

        return taskMaybe
    }

    private fun executeTasks(epochSecs: Long) {

        println("$epochSecs")
        tasks
            .filter { shouldExecute(epochSecs, it) }
            .forEach { it.runnable() }

        println()
    }

    companion object {

        private fun findBestPhase(tasks: Seq<Task>, period: Period): Phase {

            check(period > 0) { "Period must be > 0" }

            if (tasks.isEmpty()) return 0

            val (bestPhase, _) = maxExecutions(period, tasks).minBy { it.second }.getOrThrow()

            return bestPhase
        }

        private fun maxExecutions(period: Period, tasks: Seq<Task>): Seq<Pair<Phase, Int>> {

            val allSeconds = Seq.range(0, calcMaxSecs(tasks, period))
            val phases = Seq.range(0, period)

            return phases
                .map { phase ->
                    val simulatedTasks = tasks.prepend(Task("n/a", period, phase) {})
                    val maxExecutions = allSeconds
                        .map { sec -> executions(simulatedTasks, sec) }
                        .maxBy { it }
                        .getOrThrow()
                    Pair(phase, maxExecutions)
                }
        }

        internal fun calcMaxSecs(tasks: Seq<Task>, period: Period): Long = tasks
            .map(Task::period)
            .prepend(period)
            .distinct()
            .foldl(1L) { l, r -> l * r }

        internal fun executions(tasks: Seq<Task>, sec: Long): Int = tasks
            .foldl(0) { memo, task ->
                if (shouldExecute(sec, task)) memo + 1 else memo
            }

        private fun shouldExecute(epochSecs: Long, task: Task) =
            epochSecs % task.period == task.phase
    }
}