package com.noser.haskell

fun <A, B, C> flipArgs(f: (A, B) -> C): (B, A) -> C =
    { b, a -> f(a, b) }

fun <A, B, R> curry(f: (A, B) -> R): (A) -> (B) -> R =
    { a -> { b -> f(a, b) } }

fun <A, B, C, R> curry(f: (A, B, C) -> R): (A) -> (B) -> (C) -> R =
    { a -> { b -> { c -> f(a, b, c) } } }

fun <A, B, C, D, R> curry(f: (A, B, C, D) -> R): (A) -> (B) -> (C) -> (D) -> R =
    { a -> { b -> { c -> { d -> f(a, b, c, d) } } } }

