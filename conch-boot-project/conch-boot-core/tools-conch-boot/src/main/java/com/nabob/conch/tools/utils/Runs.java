package com.nabob.conch.tools.utils;

import com.nabob.conch.tools.function.RunnableWithError;
import com.nabob.conch.tools.functions.ThrowaleCallable;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class Runs {

    public static void safeDo(RunnableWithError runnable) {
        try {
            runnable.run();
        } catch (Throwable ignored) {
        }
    }

    public static void uncheckDo(RunnableWithError runnable) {
        try {
            runnable.run();
        } catch (Throwable e) {
            ExceptionUtils.wrapAndThrow(e);
        }
    }

    public static <T, E extends Throwable> T safeCall(ThrowaleCallable<T, E> callable) {
        return safeCall(callable, null);
    }

    public static <T, E extends Throwable> T safeCall(ThrowaleCallable<T, E> callable, T defaultVal) {
        try {
            return callable.call();
        } catch (Throwable e) {
            return defaultVal;
        }
    }

    public static <T, E extends Throwable> T uncheckCall(ThrowaleCallable<T, E> callable) {
        try {
            return callable.call();
        } catch (Throwable e) {
            ExceptionUtils.wrapAndThrow(e);
            return null;//不会触发
        }
    }

}
