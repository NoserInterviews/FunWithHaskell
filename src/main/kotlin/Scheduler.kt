package com.noser.haskell

import java.time.Instant
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class Scheduler(private val name: String) {

    init {
        Executors.newSingleThreadScheduledExecutor {
            Thread(it, "$name-Thread").apply { isDaemon = true }
        }.scheduleAtFixedRate({ executeTasks(Instant.now().epochSecond) }, 0L, 1L, TimeUnit.SECONDS)
    }

    @Volatile
    private var tasks: Seq<Task> = Seq.empty()

    @Synchronized
    fun addTask(id: String, period: Long, runnable: () -> Unit): Maybe<Task> {

        if (period < 1) return Maybe.nothing()

        val task = Task(id, period, findBestPhase(tasks,period), runnable)
        tasks = tasks.prepend(task)
        return Maybe.just(task)
    }

    private fun executeTasks(epochSecs: Long) {

        println("$epochSecs")
        tasks
            .filter { shouldExecute(epochSecs,it) }
            .forEach {
                print("Executing $it...")
                it.runnable()
                println("done")
            }

        println()

    }

    companion object {

        private fun findBestPhase(tasks: Seq<Task>, period: Long) : Long {

            val phases = generatePhases(period)
            val maxExecutions = maxExecutions(phases, tasks)
            return bestPhase(maxExecutions)
        }

        private fun generatePhases(period: Long) : Seq<Long> {
            (0 until period).forEach {
                TODO()
            }
        }

        private fun maxExecutions(phases : Seq<Long>, tasks: Seq<Task>) : Seq<Pair<Long,Int>> = TODO()

        private fun bestPhase(maxExecutions : Seq<Pair<Long,Int>>) : Long = TODO()

        private fun countExecutions(tasks : Seq<Task>, index: Long) : Int = TODO("if shouldExecute +1")

        private fun generateIndices(maxIndex : Long) : Seq<Long> = TODO("0,1,2,...maxIndex-1")

        private fun maxIndex(periods : Seq<Long>) : Long = TODO("multiply all periods")

        private fun periods(tasks: Seq<Task>) : Seq<Long> = TODO("find all distinct periods")

        private fun shouldExecute(epochSecs: Long, task: Task) = epochSecs % task.period == task.phase
    }


}