package com.noser.haskell

import java.time.Instant
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

private typealias Period = Long
private typealias Phase = Long
private typealias Index = Int
private typealias Execution = Int

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

        // TODO if id already present then Maybe.nothing()

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

        private fun findBestPhase(tasks: Seq<Task>, period: Period) : Phase {

            val phases = generatePhases(period)
            val maxExecutions = maxExecutions(period, phases, tasks)
            return bestPhase(maxExecutions)
        }

        private fun generatePhases(period: Period) : Seq<Phase> {
            var i = period
            var res = Seq.empty<Phase>()
            while (--i >= 0) {
                res = res.prepend(i)
            }
            return res
        }

        private fun maxExecutions(period : Period, phases : Seq<Phase>, tasks: Seq<Task>) : Seq<Pair<Phase,Execution>> {

            val maxIndex = maxIndex(periods(tasks).prepend(period))

            val indices = generateIndices(maxIndex)

            // nimm die erste phase und gehe von anfang an durch die indizes
            //     bei jedem index gucke wieviele tasks ausgeführt würden und merke dir das max
            //     wiederhole das für jede phase

            TODO()
        }

        private fun maxIndex(periods : Seq<Period>) : Index {
            TODO("multiply all periods")
            //     werfe duplikate raus
            //     multipliziere sie alle miteinander
        }

        private fun periods(tasks: Seq<Task>) : Seq<Phase> {



            TODO("find all distinct periods")
        }

        private fun generateIndices(maxIndex : Index) : Seq<Index> {
            TODO("0,1,2,...maxIndex-1")
        }

        private fun bestPhase(maxExecutions : Seq<Pair<Phase,Execution>>) : Long = TODO()

        private fun countExecutions(index: Index, tasks: Seq<Task>) : Int = TODO("if shouldExecute +1")

        private fun shouldExecute(epochSecs: Long, task: Task) = epochSecs % task.period == task.phase
    }
}