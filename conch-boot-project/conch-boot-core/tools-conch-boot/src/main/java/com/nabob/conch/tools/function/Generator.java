package com.nabob.conch.tools.function;


@FunctionalInterface
public interface Generator<T> {
    T generate();
}
