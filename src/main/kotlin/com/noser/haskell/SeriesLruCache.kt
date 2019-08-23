package com.noser.haskell

class SeriesLruCache<K : Any, V : Any> private constructor(
    values: Seq<Pair<K, V>>,
    private val maxSize: Int
) : LruCache<K, V> {

    private val values: Seq<Pair<K, V>>

    init {
        require(maxSize >= 1) { "Cache size must be > 0" }
        this.values = values.take(maxSize)
    }

    override fun get(key: K): Maybe<Pair<V, LruCache<K, V>>> {

        tailrec fun go(visitedReversed: Seq<Pair<K, V>>, remaining: Seq<Pair<K, V>>): Maybe<Pair<V, LruCache<K, V>>> =
            when {
                remaining.isEmpty() -> Maybe.nothing()
                remaining.head().first == key -> Maybe
                    .just(remaining.head().second)
                    .map {
                        val (head, tail) = remaining
                        val updated = visitedReversed.foldl(tail, Seq<Pair<K, V>>::prepend).prepend(head)
                        Pair(it, SeriesLruCache(updated, maxSize))
                    }
                else -> go(visitedReversed.prepend(remaining.head()), remaining.tail())
            }

        return go(Seq.empty(), this.values)
    }

    override fun put(key: K, value: V): LruCache<K, V> {

        return get(key)
            .map { it.second }
            .getOrElse(SeriesLruCache(values.prepend(Pair(key, value)), maxSize))
    }

    override fun evict(key: K): LruCache<K, V> = get(key)
        .map { (_, cache) ->
            cache as SeriesLruCache<K, V>
            SeriesLruCache(cache.values.tail(), maxSize)
        }
        .getOrElse(this)

    override fun size(): Int {

        return values.size()
    }

    override fun toString(): String {

        return "SeriesLruCache$values"
    }

    companion object {

        fun <K : Any, V : Any> withSize(maxSize: Int): LruCache<K, V> {

            return SeriesLruCache(Seq.empty(), maxSize)
        }
    }
}
