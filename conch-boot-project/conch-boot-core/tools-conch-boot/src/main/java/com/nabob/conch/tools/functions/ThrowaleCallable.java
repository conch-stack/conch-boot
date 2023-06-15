package com.nabob.conch.tools.functions;

public interface ThrowaleCallable<T, E extends Throwable> extends ThrowableSupplier {

    /**
     * Gets a result.
     *
     * @return a result
     * @throws E an exception
     */
    T call() throws E;

    @Override
    default Object get() throws Throwable {
        return call();
    }
}
