package com.noser.haskell;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

import static java.util.Objects.requireNonNull;

@SuppressWarnings({"unused", "WeakerAccess"})
public class SeriesLruCache<K, V> implements LruCache<K, V> {

    private final Series<Pair<K, V>> values;

    private final int maxSize;

    public static <K, V> LruCache<K, V> withSize(int maxSize) {

        return new SeriesLruCache<>(Series.empty(), maxSize);
    }

    private SeriesLruCache(Series<Pair<K, V>> values, int maxSize) {

        if (maxSize < 1) {
            throw new IllegalArgumentException("Cache size must be > 0");
        }
        this.values = values.take(maxSize);
        this.maxSize = maxSize;
    }

    @NotNull
    @Override
    public Maybe<Pair<V, LruCache<K, V>>> get(@NotNull K key) {

        requireNonNull(key);

        Series<Pair<K, V>> beforeR = Series.empty();
        Series<Pair<K, V>> remaining = values;

        while (!remaining.isEmpty()) {
            Pair<K, V> head = remaining.head();
            if (head.first.equals(key)) {
                Series<Pair<K, V>> updatedValues = beforeR.foldl(remaining.tail(), Series::prepend).prepend(head);
                return Maybe.just(Pair.of(head.second, new SeriesLruCache<>(updatedValues, maxSize)));
            }
            beforeR = beforeR.prepend(head);
            remaining = remaining.tail();
        }
        // remaining is empty -> key is not contained
        return Maybe.nothing();
    }

    @NotNull
    @Override
    public Outcome<Pair<V, LruCache<K, V>>> getOrLoad(@NotNull K key, @NotNull Function<K, V> loadFunction) {

        requireNonNull(key);
        requireNonNull(loadFunction);

        return get(key)
                .toOutcome()
                .recover(t -> Outcome
                        .of(() -> loadFunction.apply(key))
                        .map(value -> {
                            Series<Pair<K, V>> updatedValues = values.prepend(Pair.of(key, value));
                            return Pair.of(value, new SeriesLruCache<>(updatedValues, maxSize));
                        }));
    }

    @NotNull
    @Override
    public LruCache<K, V> put(@NotNull K key, @NotNull V value) {

        requireNonNull(key);
        requireNonNull(value);

        Series<Pair<K, V>> updatedValues = get(key)
                .map(pair -> ((SeriesLruCache<K, V>) pair.second).values
                        .tail()
                        .prepend(Pair.of(key, value)))
                .getOrElse(() -> values)
                .prepend(Pair.of(key, value));

        return new SeriesLruCache<>(updatedValues, maxSize);
    }

    @NotNull
    @Override
    public LruCache<K, V> evict(@NotNull K key) {

        requireNonNull(key);
        return get(key)
                .map(pair -> {
                    SeriesLruCache<K, V> cache = (SeriesLruCache<K, V>) pair.second;
                    Series<Pair<K, V>> updatedValues = cache.values.tail();
                    return new SeriesLruCache<>(updatedValues, maxSize);
                })
                .getOrElse(() -> this);
    }

    @Override
    public int size() {

        return values.size();
    }

    @Override
    public String toString() {

        return "SeriesLruCache" + values;
    }
}
