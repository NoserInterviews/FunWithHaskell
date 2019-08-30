package com.noser.haskell

sealed class Seq<V : Any> {

    abstract fun head() : V

    abstract fun tail() : Seq<V>

    abstract fun isEmpty() : Boolean

    fun prepend(v: V): Seq<V> = Cons(v, this)

    fun filter(predicate : (V) -> Boolean) : Seq<V> {

        var vs = this
        var res : Seq<V> = empty()
        while (vs != EMPTY) {
            if (predicate(vs.head())) res = res.prepend(vs.head())
            vs = vs.tail()
        }
        return res // ist invertiert!!!!
    }

    fun forEach(consumer : (V) -> Unit) {
        var vs = this
        while (vs != EMPTY) {
            consumer(vs.head())
            vs = vs.tail()
        }
    }

    companion object {
        private val EMPTY: Seq<Nothing> = Empty()

        @Suppress("UNCHECKED_CAST")
        fun <V : Any> empty(): Seq<V> = EMPTY as Seq<V>

        fun <V : Any> of(v: V): Seq<V> = Cons(v, empty())


        fun <U : Any, V : Any> map(f : (V) -> U, vs : Seq<V>) : Seq<U> {

            var res : Seq<U> = empty()
            var rem = vs
            while (!rem.isEmpty()) {
                val head = rem.head()
                res = Cons(f(head),res)
                rem = rem.tail()
            }
            return res
        }

        fun <U, V : Any> foldl(init : U, f : (U,V) -> U, vs : Seq<V>) : U {

            var res = init
            var rem = vs
            while (!rem.isEmpty()) {
                res = f(res,rem.head())
                rem = rem.tail()
            }
            return res
        }





        fun <U, V : Any> foldr(init : U, f : (V,U) -> U, vs : Seq<V>) : U {

            return when {
                vs.isEmpty() -> init
                else -> f(vs.head(), foldr(init,f,vs.tail()))
            }
        }





    }

    private class Empty<V : Any> : Seq<V>() {

        override fun head(): V = throw UnsupportedOperationException("head() on empty Seq called")

        override fun tail() : Seq<V> = throw UnsupportedOperationException("head() on empty Seq called")

        override fun isEmpty(): Boolean = true

        override fun toString(): String = "[]"
    }

    private class Cons<V : Any>(private val head: V, private val tail: Seq<V>) : Seq<V>() {

        override fun head(): V = head

        override fun tail(): Seq<V> = tail

        override fun isEmpty(): Boolean = false

        override fun toString(): String {
            return "[$head,...]"
        }
    }
}