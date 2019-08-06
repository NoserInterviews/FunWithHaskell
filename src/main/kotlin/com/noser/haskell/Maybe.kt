package com.noser.haskell

sealed class Maybe<V : Any> {

    abstract fun getOrThrow() : V

    abstract fun getOrElse(alt: V): V

    companion object {

        private val NOT = Not<Nothing>()

        @Suppress("UNCHECKED_CAST")
        fun <V : Any> nothing(): Maybe<V> = NOT as Maybe<V>

        fun <V : Any> just(v: V): Maybe<V> = Just(v)

        fun <V : Any> of(v: V?): Maybe<V> = if (v != null) just(v) else nothing()
    }

    private class Not<V : Any> : Maybe<V>() {

        override fun getOrThrow(): V = throw NoSuchElementException("getOrThrow() called on Nothing")

        override fun getOrElse(alt: V): V = alt

        override fun toString(): String = "Nothing"

        override fun equals(other: Any?): Boolean = this === other

        override fun hashCode(): Int = javaClass.hashCode()
    }

    private class Just<V : Any>(private val value: V) : Maybe<V>() {

        override fun getOrThrow(): V = value

        override fun getOrElse(alt: V): V = value

        override fun toString(): String = "Just($value)"

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Just<*>

            return value == other.value
        }

        override fun hashCode(): Int {
            return value.hashCode()
        }
    }
}