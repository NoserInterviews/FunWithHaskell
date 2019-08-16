package com.noser.haskell;

@FunctionalInterface
public interface ThrowingFunction<U, V> {

    V apply(U u) throws Exception;
}
