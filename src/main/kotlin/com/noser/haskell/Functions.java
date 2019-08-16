package com.noser.haskell;

import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("WeakerAccess")
public abstract class Functions {

    private Functions() { }

    @NotNull
    public static <A, B, C> BiFunction<B, A, C> flipArgs(@NotNull BiFunction<A, B, C> fun) {

        requireNonNull(fun);
        return (B b, A a) -> fun.apply(a, b);
    }
}
