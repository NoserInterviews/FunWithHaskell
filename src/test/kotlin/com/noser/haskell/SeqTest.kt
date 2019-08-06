package com.noser.haskell

import org.junit.Test
import kotlin.test.assertEquals

internal class SeqTest {

    @Test
    fun testRange() {

        assertEquals(
            Seq.ofAll(0L, 1L, 2L),
            Seq.range(0, 3)
        )

        assertEquals(
            Seq.ofAll(2L, 1L, 0L),
            Seq.range(2, -1)
        )
    }

    @Test
    fun testMaxBy() {

        assertEquals(Maybe.just("three"), Seq.ofAll("one", "two", "three").maxBy { it.length })
    }

    @Test
    fun testMinBy() {

        assertEquals(Maybe.just("tw"), Seq.ofAll("one", "tw", "three").minBy { it.length })
    }

}