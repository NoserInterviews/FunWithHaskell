package com.noser.haskell

object Main {

    @JvmStatic
    fun main(args: Array<String>) {

        // TODO generate some random tasks and then print them out
        Scheduler("important-stuff").addTask("gaga", 10, { })
        Thread.sleep(15_000)
    }

}
