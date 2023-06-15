package com.nabob.conch.tools.utils;

import java.util.function.Predicate;

/**
 * Created by tangming on 5/25/17.
 */
public class ObjectUtils {

    public static <T> T predictAs(T bean, T as, Predicate<T> predicate) {
        return predicate.test(bean) ? as : bean;
    }
}
