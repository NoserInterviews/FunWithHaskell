package com.noser.haskell

sealed class Seq<V : Any> {

    fun prepend(v: V): Seq<V> = Cons(v, this)

    companion object {
        private val EMPTY: Seq<Nothing> = Empty()

        @Suppress("UNCHECKED_CAST")
        fun <V : Any> empty(): Seq<V> = EMPTY as Seq<V>

        fun <V : Any> of(v: V): Seq<V> = Cons(v, empty())
    }

    private class Empty<V : Any> : Seq<V>() {

        override fun toString(): String = "[]"
    }

    private class Cons<V : Any>(private val head: V, private val tail: Seq<V>) : Seq<V>() {

        override fun toString(): String {
            return "[$head,...]"
        }
    }
}