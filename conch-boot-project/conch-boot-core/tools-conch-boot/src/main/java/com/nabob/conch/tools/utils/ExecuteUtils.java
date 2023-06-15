package com.nabob.conch.tools.utils;

import com.nabob.conch.tools.function.RunnableWithError;

import java.io.Closeable;

public class ExecuteUtils {

    public static void closeIfPossible(Closeable... closeables) {
        for (Closeable closeable : closeables) {
            runIgnoreError(closeable::close);
        }
    }

    /**
     * @see Runs#safeDo(com.nabob.conch.tools.function.RunnableWithError)
     */
    @Deprecated
    public static void runIgnoreError(RunnableWithError runnable) {
        try {
            runnable.run();
        } catch (Throwable ignored) {
        }
    }
}
