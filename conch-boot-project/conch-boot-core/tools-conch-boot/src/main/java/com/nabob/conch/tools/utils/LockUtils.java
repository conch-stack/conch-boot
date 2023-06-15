package com.nabob.conch.tools.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;


public class LockUtils {
    /**
     * 使用锁同步执行代码
     *
     * @param lock   -同步锁
     * @param action -执行操作
     */
    public static void runWithLock(Lock lock, Runnable action) {
        lock.lock();
        try {
            action.run();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 尝试使用锁同步执行代码
     *
     * @param lock   -同步锁
     * @param action -执行操作
     * @return {@code true} if the lock was acquired and
     * {@code false} otherwise
     */
    public static boolean tryRunWithLock(Lock lock, Runnable action) {
        if (lock.tryLock()) {
            try {
                action.run();
            } finally {
                lock.unlock();
            }
            return true;
        } else {
            return false;
        }
    }


    /**
     * 使用锁同步执行代码
     *
     * @param lock     -同步锁
     * @param callable -A task that returns a result
     * @param <R>      －the result type of method {@code call}
     * @return -the result of the task and {@code null} if the task failed
     */
    public static <R> R callWithLock(Lock lock, Callable<R> callable) {
        return callWithLock(lock, callable, null);
    }

    public static <R> R callWithLock(Lock lock, Callable<R> callable, R defaultValue) {
        lock.lock();
        try {
            return callable.call();
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        } finally {
            lock.unlock();
        }
    }
}
