package com.noser.haskell

sealed class Maybe<V : Any> {

    companion object {

        private val NOT = Not<Nothing>()

        @Suppress("UNCHECKED_CAST")
        fun <V : Any> nothing(): Maybe<V> = NOT as Maybe<V>

        fun <V : Any> just(v: V): Maybe<V> = Just(v)
    }

    private class Not<V : Any> : Maybe<V>() {

        override fun toString(): String = "Nothing"
    }

    private class Just<V : Any>(private val v: V) : Maybe<V>() {

        override fun toString(): String = "Just $v"
    }
}