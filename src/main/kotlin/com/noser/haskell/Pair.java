package com.noser.haskell;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

@SuppressWarnings({"WeakerAccess", "unused"})
public final class Pair<A, B> {

    public final A first;
    public final B second;

    private Pair() {

        // deserialization
        this(null, null);
    }

    private Pair(A first, B second) {

        this.first = first;
        this.second = second;
    }

    @NotNull
    public static <U,V> Pair<U,V> of(U u, V v) {

        return new Pair<>(u, v);
    }

    public A getFirst() {

        return first;
    }

    public B getSecond() {

        return second;
    }

    /**
     * Enable Kotlin destructuring
     */
    public A component1() {

        return first;
    }

    /**
     * Enable Kotlin destructuring
     */
    public B component2() {

        return second;
    }

    @NotNull
    public <C,D> Pair<C,D> map(@NotNull Function<A,C> f1,
                               @NotNull Function<B,D> f2) {

        requireNonNull(f1);
        requireNonNull(f2);
        return new Pair<>(f1.apply(first), f2.apply(second));
    }

    @NotNull
    public <C> Pair<C, B> map1(@NotNull Function<A, C> f) {

        requireNonNull(f);
        return new Pair<>(f.apply(first), second);
    }

    @NotNull
    public <C> Pair<A, C> map2(@NotNull Function<B, C> f) {

        requireNonNull(f);
        return new Pair<>(first, f.apply(second));
    }

    public <C> C transform(@NotNull BiFunction<A,B,C> f) {

        requireNonNull(f);
        return f.apply(first, second);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Pair<?, ?> other = (Pair<?, ?>) obj;
        return Objects.equals(first, other.first) &&
                Objects.equals(second, other.second);
    }

    @Override
    public int hashCode() {

        return Objects.hash(first, second);
    }

    @NotNull
    @Override
    public String toString() {

        return "Pair(" + first +
                "," + second +
                ')';
    }
}
