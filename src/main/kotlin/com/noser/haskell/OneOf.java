package com.noser.haskell;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("unused")
public abstract class OneOf<L, R> {

    private OneOf() {
        // don't instantiate
    }

    public abstract boolean isLeft();

    public abstract boolean isRight();

    public abstract L leftOrThrow();

    public abstract R rightOrThrow();

    public abstract <C> C handle(@NotNull Function<L, C> fa, @NotNull Function<R, C> fb);

    @NotNull
    public abstract <U> OneOf<U, R> mapLeft(@NotNull Function<L, U> f);

    @NotNull
    public abstract <U> OneOf<U, R> flatMapLeft(@NotNull Function<L, OneOf<U, R>> f);

    @NotNull
    public abstract <V> OneOf<L, V> mapRight(@NotNull Function<R, V> f);

    @NotNull
    public abstract <V> OneOf<L, V> flatMapRight(@NotNull Function<R, OneOf<L, V>> f);

    @NotNull
    public abstract OneOf<L, R> onLeft(@NotNull Consumer<L> leftConsumer);

    @NotNull
    public abstract OneOf<L, R> onRight(@NotNull Consumer<R> rightConsumer);

    @NotNull
    public static <A, B> OneOf<A, B> left(@NotNull A value) {

        return new Left<>(value);
    }

    @NotNull
    public static <A, B> OneOf<A, B> right(@NotNull B value) {

        return new Right<>(value);
    }

    private static class Left<A, B> extends OneOf<A, B> {

        private final A value;

        private Left(A value) {

            this.value = value;
        }

        @Override
        public boolean isLeft() {

            return true;
        }

        @Override
        public boolean isRight() {

            return false;
        }

        @Override
        public A leftOrThrow() {

            return value;
        }

        @Override
        public B rightOrThrow() {

            throw new IllegalStateException("rightOrThrow() called on Left");
        }

        @Override
        public <C> C handle(@NotNull Function<A, C> fa, @NotNull Function<B, C> fb) {

            return requireNonNull(requireNonNull(fa).apply(value));
        }

        @NotNull
        @Override
        public <U> OneOf<U, B> mapLeft(@NotNull Function<A, U> f) {

            return left(requireNonNull(f).apply(value));
        }

        @NotNull
        @Override
        public <U> OneOf<U, B> flatMapLeft(@NotNull Function<A, OneOf<U, B>> f) {

            return requireNonNull(f.apply(value));
        }

        @NotNull
        @Override
        public <V> OneOf<A, V> mapRight(@NotNull Function<B, V> f) {

            return left(value);
        }

        @NotNull
        @Override
        public <V> OneOf<A, V> flatMapRight(@NotNull Function<B, OneOf<A, V>> f) {

            return left(value);
        }

        @NotNull
        @Override
        public OneOf<A, B> onLeft(@NotNull Consumer<A> leftConsumer) {

            requireNonNull(leftConsumer).accept(value);
            return this;
        }

        @NotNull
        @Override
        public OneOf<A, B> onRight(@NotNull Consumer<B> rightConsumer) {

            return this;
        }
    }

    private static class Right<L, R> extends OneOf<L, R> {

        private final R value;

        private Right(R value) {

            this.value = value;
        }

        @Override
        public boolean isLeft() {

            return false;
        }

        @Override
        public boolean isRight() {

            return true;
        }

        @Override
        public L leftOrThrow() {

            throw new IllegalStateException("leftOrThrow() called on Right");
        }

        @Override
        public R rightOrThrow() {

            return value;
        }

        @Override
        public <C> C handle(@NotNull Function<L, C> fa, @NotNull Function<R, C> fb) {

            return requireNonNull(requireNonNull(fb).apply(value));
        }

        @NotNull
        @Override
        public <U> OneOf<U, R> mapLeft(@NotNull Function<L, U> f) {

            return right(value);
        }

        @NotNull
        @Override
        public <U> OneOf<U, R> flatMapLeft(@NotNull Function<L, OneOf<U, R>> f) {

            return right(value);
        }

        @NotNull
        @Override
        public <V> OneOf<L, V> mapRight(@NotNull Function<R, V> f) {

            return right(requireNonNull(f).apply(value));
        }

        @NotNull
        @Override
        public <V> OneOf<L, V> flatMapRight(@NotNull Function<R, OneOf<L, V>> f) {

            return requireNonNull(f.apply(value));
        }

        @NotNull
        @Override
        public OneOf<L, R> onLeft(@NotNull Consumer<L> leftConsumer) {

            return this;
        }

        @NotNull
        @Override
        public OneOf<L, R> onRight(@NotNull Consumer<R> rightConsumer) {

            requireNonNull(rightConsumer).accept(value);
            return this;
        }
    }
}
