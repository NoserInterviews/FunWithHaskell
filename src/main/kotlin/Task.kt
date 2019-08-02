package com.noser.haskell

data class Task(
    val id : String,
    val period: Long,
    val phase: Long,
    val runnable: () -> Unit
) {
    override fun toString(): String = "Task(id=$id,period=$period,phase=$phase)"
}