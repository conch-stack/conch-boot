package com.nabob.conch.tools.security;

public interface Decryptor<T, R> {

    R decrypt(T input, Object... params) throws Throwable;
}
