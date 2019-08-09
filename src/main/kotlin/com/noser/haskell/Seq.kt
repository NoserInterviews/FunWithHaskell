package com.noser.haskell

@Suppress("MemberVisibilityCanBePrivate")
sealed class Seq<V : Any> {

    abstract fun head(): V

    abstract fun tail(): Seq<V>

    abstract fun isEmpty(): Boolean

    abstract fun size(): Int

    fun prepend(v: V): Seq<V> = Cons(v, this)

    fun filter(predicate: (V) -> Boolean): Seq<V> =
        foldr(empty()) { v, seq -> if (predicate(v)) seq.prepend(v) else seq }

    fun contains(v: V) = any { it == v }

    fun distinct(): Seq<V> =
        foldr(empty()) { v, memo -> if (!memo.contains(v)) memo.prepend(v) else memo }

    fun any(predicate: (V) -> Boolean): Boolean =
        foldl(false) { memo, v -> memo || predicate(v) }

    fun forEach(consumer: (V) -> Unit) {
        tailrec fun go(vs: Seq<V>) {
            if (!vs.isEmpty()) {
                consumer(vs.head())
                go(vs.tail())
            }
        }
        go(this)
    }

    fun <U : Any> map(f: (V) -> U): Seq<U> = foldr(empty()) { v, seq -> seq.prepend(f(v)) }

    fun <U : Comparable<U>> maxBy(f: (V) -> U): Maybe<V> =
        if (isEmpty())
            Maybe.nothing()
        else Maybe.maybe(
            tail()
                .foldl(Pair(head(), f(head()))) { pair, v ->
                    val fV = f(v)
                    if (fV > pair.second) Pair(v, fV) else pair
                }.first
        )

    fun <U : Comparable<U>> minBy(f: (V) -> U): Maybe<V> =
        if (isEmpty())
            Maybe.nothing()
        else Maybe.maybe(
            tail()
                .foldl(Pair(head(), f(head()))) { pair, v ->
                    val fV = f(v)
                    if (fV < pair.second) Pair(v, fV) else pair
                }.first
        )

    fun <U> foldr(init: U, f: (V, U) -> U): U {

        fun go(rem: Seq<V>): U =
            when {
                rem.isEmpty() -> init
                else          -> f(rem.head(), go(rem.tail()))
            }

        return if (size() > 1000) reverse().foldl(init, flipArgs(f)) else go(this)
    }

    fun reverse(): Seq<V> = foldl(empty(), Seq<V>::prepend)

    fun <U> foldl(init: U, f: (U, V) -> U): U {

        tailrec fun go(memo: U, vs: Seq<V>): U =
            when {
                vs.isEmpty() -> memo
                else         -> go(f(memo, vs.head()), vs.tail())
            }

        return go(init, this)
    }

    companion object {
        private val EMPTY: Seq<Nothing> = Empty()

        @Suppress("UNCHECKED_CAST")
        fun <V : Any> empty(): Seq<V> = EMPTY as Seq<V>

        fun <V : Any> of(v: V): Seq<V> = Cons(v, empty())

        fun <V : Any> ofAll(vararg vs: V): Seq<V> {

            var res = empty<V>()
            (vs.size - 1 downTo 0).forEach {
                res = res.prepend(vs[it])
            }
            return res
        }

        fun range(start: Long, endExcl: Long): Seq<Long> =
            if (start > endExcl)
                range(endExcl + 1, start + 1).reverse()
            else {
                var i = endExcl
                var res = empty<Long>()
                while (--i >= start) {
                    res = res.prepend(i)
                }
                res
            }

        fun <V : Any> flip(mvs: Seq<Maybe<V>>): Maybe<Seq<V>> =
            mvs.foldr(Maybe.just(empty())) { mv, memo -> memo.flatMap { vs -> mv.map(vs::prepend) } }

        fun <V : Any> flip(tvs: Seq<Try<V>>): Try<Seq<V>> =
            tvs.foldr(Try.success(empty())) { tv, memo -> memo.flatMap { vs -> tv.map(vs::prepend) } }
    }

    private class Empty<V : Any> : Seq<V>() {

        override fun head(): V = throw UnsupportedOperationException("head() on empty Seq called")

        override fun tail(): Seq<V> = throw UnsupportedOperationException("head() on empty Seq called")

        override fun isEmpty(): Boolean = true

        override fun size(): Int = 0

        override fun toString(): String = "[]"

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            return true
        }

        override fun hashCode(): Int {
            return javaClass.hashCode()
        }
    }

    class Cons<V : Any>(private val head: V, private val tail: Seq<V>) : Seq<V>() {

        private val size = tail.size() + 1

        override fun head(): V = head

        override fun tail(): Seq<V> = tail

        override fun isEmpty(): Boolean = false

        override fun size(): Int = size

        override fun toString(): String {
            return "[$head,...]"
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Seq<*>

            tailrec fun go(l: Seq<*>, r: Seq<*>): Boolean =
                when {
                    l.isEmpty() && r.isEmpty() -> true
                    l.isEmpty() || r.isEmpty() -> false
                    l.head() == r.head()       -> go(l.tail(), r.tail())
                    else                       -> false
                }

            return go(this, other)
        }

        override fun hashCode(): Int = tail
            .foldl(head.hashCode()) { memo, v -> 31 * memo + v.hashCode() }
    }
}