package com.noser.haskell;

import kotlin.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

@SuppressWarnings("unused")
public interface LruCache<K, V> {

    /**
     * Looks for the entry associated with the given key.
     * <p>
     * If found, the Cache is updated, thus it is returned as well.
     */
    @NotNull
    Maybe<Pair<V, LruCache<K, V>>> get(@NotNull K key);

    /**
     * Looks for the entry associated with the given key.
     * <p/>
     * If the entry is not found, calls the loadFunction in order to procure it and insert
     * it into the cache.
     * <p/>
     * Returns an Outcome of the {@link #get(K)} call, because the loading might fail.
     */
    @NotNull
    Try<Pair<V, LruCache<K, V>>> getOrLoad(@NotNull K key, @NotNull Function<K, V> loadFunction);

    /**
     * Returns a Cache containing an entry associated with the given key that has the given value.
     */
    @NotNull
    LruCache<K, V> put(@NotNull K key, @NotNull V value);

    /**
     * Returns a Cache in which doesn't contain an entry associated with the given key.
     */
    @NotNull
    LruCache<K, V> evict(@NotNull K key);

    /**
     * Returns the number of entries in the cache.
     */
    int size();
}
