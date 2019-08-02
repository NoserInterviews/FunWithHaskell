package com.noser.haskell

object Main {

    @JvmStatic
    fun main(args: Array<String>) {

        val scheduler = Scheduler("important-stuff")

        scheduler.addTask("task1", 3) { print("dududu...") }
        scheduler.addTask("task2", 3) { print("dadada...") }

        Thread.sleep(16_000)
    }

}
