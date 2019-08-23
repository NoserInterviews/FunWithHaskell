package com.noser.haskell

import com.noser.haskell.Seq.Companion.empty
import com.noser.haskell.Seq.Cons
import org.junit.Test
import kotlin.test.assertEquals


internal class SchedulerTest {

    @Test
    fun testMaxSeconds() {

        val runnable = {}

        val tasks: Seq<Task> = Cons(Task("1", 3L, 0L, runnable),
            Cons(Task("1", 7L, 0L, runnable), empty())
        )

        assertEquals(105L,Scheduler.calcMaxSecs(tasks,5))
    }
}