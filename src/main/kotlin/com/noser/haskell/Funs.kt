package com.noser.haskell

fun <A, B, C> flipArgs(f: (A, B) -> C): (B, A) -> C = { b, a -> f(a, b) }