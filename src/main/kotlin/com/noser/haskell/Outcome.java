package com.noser.haskell;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class Outcome<T> {

    private Outcome() { /* private so cannot be instantiated from outside */ }

    /**
     * map() will incorporate exceptions during the execution of the ThrowingFunction into the
     * resulting Outcome.
     */
    @NotNull
    public abstract <V> Outcome<V> map(@NotNull ThrowingFunction<? super T, V> mapper);

    /**
     * flatMap() expects the mapper to handle execptions itself.
     */
    @NotNull
    public abstract <V> Outcome<V> flatMap(@NotNull Function<? super T, Outcome<V>> mapper);

    public abstract Outcome<T> recover(@NotNull Function<Throwable, Outcome<T>> recoverer);

    public abstract T getOrThrow();

    public abstract T getOrElse(@NotNull Function<Throwable, T> alternative);

    public abstract boolean isSuccess();

    public abstract boolean isFailure();

    @NotNull
    public abstract Outcome<T> onSuccess(@NotNull Consumer<? super T> consumer);

    @NotNull
    public abstract Outcome<T> onFailure(@NotNull Consumer<Throwable> consumer);

    @NotNull
    public abstract <L> OneOf<L, T> toOneOf(@NotNull Function<Throwable, L> leftTransform);

    @NotNull
    public abstract Maybe<T> toMaybe();

    @NotNull
    public abstract Series<T> toSeries();

    @NotNull
    public static <T> Outcome<T> success(T value) { return new Success<>(value); }

    @NotNull
    public static <T> Outcome<T> failure(@NotNull Throwable throwable) {

        return new Failure<>(requireNonNull(throwable));
    }

    @NotNull
    public static <T> Outcome<T> of(@NotNull ThrowingSupplier<? extends T> supplier) {

        try {
            return success(requireNonNull(supplier).get());
        } catch (Throwable t) {
            return failure(t);
        }
    }

    @NotNull
    public static <T> Outcome<T> from(@NotNull CompletableFuture<T> future,
                                      long timeout,
                                      @NotNull TimeUnit timeUnit) {

        requireNonNull(future);
        requireNonNull(timeUnit);

        try {
            return Outcome.success(future.get(timeout, timeUnit));
        } catch (Exception e) {
            return Outcome.failure(e);
        }
    }

    private final static class Success<T> extends Outcome<T> {

        private final T value;

        private Success(T value) {

            this.value = value;
        }

        @Override
        @NotNull
        public <V> Outcome<V> map(@NotNull ThrowingFunction<? super T, V> mapper) {

            try {
                return success(requireNonNull(mapper).apply(value));
            } catch (Throwable t) {
                return failure(t);
            }
        }

        @Override
        @NotNull
        public <V> Outcome<V> flatMap(@NotNull Function<? super T, Outcome<V>> mapper) {

            return requireNonNull(requireNonNull(mapper).apply(value));
        }

        @Override
        public Outcome<T> recover(@NotNull Function<Throwable, Outcome<T>> recoverer) {

            return this;
        }

        @Override
        public T getOrThrow() {

            return value;
        }

        @Override
        public T getOrElse(@NotNull Function<Throwable, T> alternative) {

            return value;
        }

        @Override
        public boolean isSuccess() {

            return true;
        }

        @Override
        public boolean isFailure() {

            return false;
        }

        @Override
        @NotNull
        public Outcome<T> onSuccess(@NotNull Consumer<? super T> consumer) {

            requireNonNull(consumer).accept(value);
            return this;
        }

        @Override
        @NotNull
        public Outcome<T> onFailure(@NotNull Consumer<Throwable> consumer) {

            return this;
        }

        @NotNull
        @Override
        public <L> OneOf<L, T> toOneOf(@NotNull Function<Throwable, L> leftTransform) {

            return OneOf.right(value);
        }

        @NotNull
        @Override
        public Maybe<T> toMaybe() {

            return Maybe.of(value);
        }

        @NotNull
        @Override
        public Series<T> toSeries() {

            return value == null ? Series.empty() : Series.of(value);
        }
    }

    private final static class Failure<T> extends Outcome<T> {

        @NotNull
        private final Throwable throwable;

        private Failure(@NotNull Throwable throwable) {

            this.throwable = requireNonNull(throwable);
        }

        @NotNull
        @Override
        public <V> Outcome<V> map(@NotNull ThrowingFunction<? super T, V> mapper) {

            return failure(throwable);
        }

        @NotNull
        @Override
        public <V> Outcome<V> flatMap(@NotNull Function<? super T, Outcome<V>> mapper) {

            return failure(throwable);
        }

        @Override
        public Outcome<T> recover(@NotNull Function<Throwable, Outcome<T>> recoverer) {

            requireNonNull(recoverer);
            return recoverer.apply(throwable);
        }

        @Override
        public T getOrThrow() {

            if (throwable instanceof RuntimeException)
                throw (RuntimeException) throwable;
            else
                throw new RuntimeException(throwable);
        }

        @Override
        public T getOrElse(@NotNull Function<Throwable, T> alternative) {

            return requireNonNull(alternative).apply(throwable);
        }

        @Override
        public boolean isSuccess() {

            return false;
        }

        @Override
        public boolean isFailure() {

            return true;
        }

        @NotNull
        @Override
        public Outcome<T> onSuccess(@NotNull Consumer<? super T> consumer) {

            return this;
        }

        @NotNull
        @Override
        public Outcome<T> onFailure(@NotNull Consumer<Throwable> consumer) {

            requireNonNull(consumer).accept(throwable);
            return this;
        }

        @NotNull
        @Override
        public <L> OneOf<L, T> toOneOf(@NotNull Function<Throwable, L> leftTransform) {

            requireNonNull(leftTransform);
            return OneOf.left(leftTransform.apply(throwable));
        }

        @NotNull
        @Override
        public Maybe<T> toMaybe() {

            return Maybe.nothing();
        }

        @NotNull
        @Override
        public Series<T> toSeries() {

            return Series.empty();
        }
    }
}
