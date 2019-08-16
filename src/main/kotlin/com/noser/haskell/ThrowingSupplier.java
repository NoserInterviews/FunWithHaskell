package com.noser.haskell;

@FunctionalInterface
public interface ThrowingSupplier<T> {

    T get() throws Exception;
}
