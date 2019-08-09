package com.noser.haskell

sealed class Try<V : Any> {

    abstract fun <W : Any> flatMap(f: (V) -> Try<W>): Try<W>

    fun <W : Any> map(f: (V) -> W): Try<W> = flatMap { tryTo { f(it) } }

    companion object {
        fun <V : Any> success(v: V): Try<V> = Success(v)
        fun <V : Any> failure(t: Throwable): Try<V> = Failure(t)
        fun <V : Any> tryTo(f: () -> V): Try<V> = try {
            success(f())
        } catch (t: Throwable) {
            failure(t)
        }
    }

    private class Success<V : Any>(val v: V) : Try<V>() {

        override fun <W : Any> flatMap(f: (V) -> Try<W>): Try<W> = f(v)
    }

    private class Failure<V : Any>(val t: Throwable) : Try<V>() {

        override fun <W : Any> flatMap(f: (V) -> Try<W>): Try<W> = Failure(t)
    }

}