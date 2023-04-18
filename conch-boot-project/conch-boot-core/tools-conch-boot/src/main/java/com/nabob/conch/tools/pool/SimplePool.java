package com.nabob.conch.tools.pool;

import com.nabob.conch.tools.function.Generator;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.Consumer;
import java.util.function.Function;


public class SimplePool<T> implements Pool<T> {
    private Queue<T> cache;
    private Generator<T> generator;

    public SimplePool(Generator<T> generator, int capacity) {
        this.generator = generator;
        cache = new ArrayBlockingQueue<T>(capacity);
    }

    protected T getOrCreate() {
        T t = cache.poll();
        if (t == null) {
            t = generator.generate();
        }
        return t;
    }

    protected void returnT(T t) {
        cache.offer(t);
    }

    @Override
    public <R> R call(Function<T, R> function) {
        T t = getOrCreate();
        R result = function.apply(t);
        returnT(t);
        return result;
    }

    @Override
    public void run(Consumer<T> action) {
        T t = getOrCreate();
        action.accept(t);
        returnT(t);
    }
}
