package com.nabob.conch.tools.cube;

import com.nabob.conch.tools.utils.Assert;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class LightAbsent<T> implements Cube<T> {
    private static final LightAbsent<Object> EMPTY_INSTANCE = new LightAbsent<Object>();

    @SuppressWarnings("unchecked") // implementation is "fully variant"
    static <T> Cube<T> withType() {
        return (Cube<T>) EMPTY_INSTANCE;
    }

    private LightAbsent() {
    }

    @Override
    public Cube<T> onCompleted(Runnable onCompleted) {
        return Absent.<T>withType().onCompleted(onCompleted);
    }

    @Override
    public Cube<T> onCompleted(Consumer<T> onCompleted) {
        return Absent.<T>withType().onCompleted(onCompleted);
    }

    @Override
    public boolean isPresent() {
        return false;
    }

    @Override
    public void ifPresent(Consumer<T> consumer) {
    }

    @Override
    public void complete() {

    }

    @Override
    public T get() {
        throw new IllegalStateException("Cube.get() cannot be called on an absent value");
    }

    @Override
    public T or(T defaultValue) {
        return defaultValue;
    }

    @SuppressWarnings("unchecked") // safe covariant cast
    @Override
    public Cube<T> or(Cube<? extends T> secondChoice) {
        Assert.notNull(secondChoice);
        return (Cube<T>) secondChoice;
    }

    @Override
    public T or(Supplier<? extends T> supplier) {
        return supplier.get();
    }

    @Override
    @Nullable
    public T orNull() {
        return null;
    }

    @Override
    public Set<T> asSet() {
        return Collections.emptySet();
    }

    @Override
    public <V> Cube<V> map(Function<? super T, V> function) {
        return Cube.absent();
    }

    @Override
    public <V> Cube<V> flatMap(Function<? super T, Cube<V>> function) {
        return Cube.absent();
    }

    @Override
    public boolean equals(@Nullable Object object) {
        return object == this;
    }

    @Override
    public int hashCode() {
        return 0x79a31aac;
    }

    @Override
    public String toString() {
        return "Cube.LightAbsent()";
    }

    private static final long serialVersionUID = 0;
}
