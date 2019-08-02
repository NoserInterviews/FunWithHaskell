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

        val task = Task(id, period, 0L, runnable)
        tasks = tasks.prepend(task)
        return Maybe.just(task)
    }

    private fun executeTasks(epochSecs: Long) {

        println(tasks)
    }

    private fun shouldExecute(epochSecs: Long, task: Task) = epochSecs % task.period == task.phase
}