package com.nabob.conch.tools.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * which annotation by {@code DecryptSupport} should implement a method which named "decrypt"
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface DecryptSupport {
    String[] value();
}
