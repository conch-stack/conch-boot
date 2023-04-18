package com.nabob.conch.tools.parallel;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @Project: [ops]
 * @Description: [description]
 * @Author: [toming]
 * @CreateDate: [10/31/16 9:40 AM]
 * @Version: [v1.0]
 */
public class ParallelFuture<T> implements Future<T> {
    private Future<T> core;

    public ParallelFuture(Future<T> core) {
        this.core = core;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return core.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return core.isCancelled();
    }

    @Override
    public boolean isDone() {
        return core.isDone();
    }

    @Override
    public T get() {
        try {
            return core.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public T getOrDefault(T defaultValue) {

        try {
            return core.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return core.get(timeout, unit);
    }
}
