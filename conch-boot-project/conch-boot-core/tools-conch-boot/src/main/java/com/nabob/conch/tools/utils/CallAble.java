package com.nabob.conch.tools.utils;

/**
 * Created by toming on 3/10/17.
 */

public interface CallAble<T> {
    T call() throws Exception;
}
