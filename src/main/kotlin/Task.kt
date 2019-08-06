package com.noser.haskell

data class Task(
    val id: String,
    val period: Period,
    val phase: Phase,
    val runnable: () -> Unit
) {
    override fun toString(): String = "Task(id=$id,period=$period,phase=$phase)"
}