package com.noser.haskell

interface LruCache<K : Any, V : Any> {

    /**
     * Looks for the entry associated with the given key.
     *
     *
     * If found, the Cache is updated, thus it is returned as well.
     */
    operator fun get(key: K): Maybe<Pair<V, LruCache<K, V>>>

    /**
     * Looks for the entry associated with the given key.
     *
     *
     * If the entry is not found, calls the loadFunction in order to procure it and insert
     * it into the cache.
     *
     *
     * Returns an Outcome of the [.get] call, because the loading might fail.
     */
    fun getOrLoad(key: K, loadFunction: (K) -> V): Try<Pair<V, LruCache<K, V>>> =
        get(key)
            .toTry()
            .recover { Try.tryTo { loadFunction(key) }.map { Pair(it, put(key, it)) } }

    /**
     * Returns a Cache containing an entry associated with the given key that has the given value.
     */
    fun put(key: K, value: V): LruCache<K, V>

    /**
     * Returns a Cache in which doesn't contain an entry associated with the given key.
     */
    fun evict(key: K): LruCache<K, V>

    /**
     * Returns the number of entries in the cache.
     */
    fun size(): Int
}
