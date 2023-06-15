package com.nabob.conch.tools.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static java.util.concurrent.TimeUnit.MINUTES;

public class StaticFieldUtils {
    private static Logger logger = LoggerFactory.getLogger(StaticFieldUtils.class);

    private static Cache<Class, Map<String, Object>> nameIndexCache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterWrite(10, MINUTES)
            .build();
    private static Cache<Class, Map<Object, String>> valueIndexCache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterWrite(10, MINUTES)
            .build();

    public static boolean existValue(Class staticClass, Object value) {
        try {
            String name = valueToName(staticClass, value);
            return name != null;
        } catch (Exception e) {
            return false;
        }
    }

    public static <T> T nameToValue(Class staticClass, String fieldName) {
        Map<String, Object> fieldMap = nameIndexCache.getIfPresent(staticClass);
        if (fieldMap == null) {
            buildFieldMap(staticClass);
            fieldMap = nameIndexCache.getIfPresent(staticClass);
        }
        Object value = fieldMap.get(fieldName);
        if (value == null) {
            throw new IllegalArgumentException("unknown field[" + fieldName +
                    "] for [" + staticClass.getName() + "]");
        } else {
            return (T) value;
        }
    }

    private static synchronized void buildFieldMap(Class staticClass) {
        logger.info("build name index cache for[" + staticClass.getName() + "]");
        if (nameIndexCache.getIfPresent(staticClass) != null)
            return;
        Map<String, Object> fieldMap = new HashMap<>();
        Field[] fields = staticClass.getDeclaredFields();
        for (Field field : fields) {
            try {
                fieldMap.put(field.getName(), field.get(null));
            } catch (IllegalAccessException ignored) {
                logger.error("error to get[" + field.getName() + "] from [" + staticClass.getName() + "]");
            }
        }
        nameIndexCache.put(staticClass, fieldMap);
    }

    public static String valueToNameSafety(Class staticClass, Object value) {
        Map<Object, String> fieldMap = valueIndexCache.getIfPresent(staticClass);
        if (fieldMap == null) {
            buildValueMap(staticClass);
            fieldMap = valueIndexCache.getIfPresent(staticClass);
        }
        String name = fieldMap.get(value);

        if (name == null) {
            return null;
        } else {
            return name;
        }
    }

    public static String valueToName(Class staticClass, Object value) {
        Map<Object, String> fieldMap = valueIndexCache.getIfPresent(staticClass);
        if (fieldMap == null) {
            buildValueMap(staticClass);
            fieldMap = valueIndexCache.getIfPresent(staticClass);
        }
        String name = fieldMap.get(value);

        if (name == null) {
            throw new IllegalArgumentException("unknown value" + String.valueOf(value) +
                    "] for [" + staticClass.getName() + "]");
        } else {
            return name;
        }
    }

    private static synchronized void buildValueMap(Class staticClass) {
        logger.info("build value index cache for[" + staticClass.getName() + "]");
        if (valueIndexCache.getIfPresent(staticClass) != null)
            return;
        Map<Object, String> valueMap = new HashMap<>();
        Field[] fields = staticClass.getDeclaredFields();
        for (Field field : fields) {
            try {
                valueMap.put(field.get(null), field.getName());
            } catch (IllegalAccessException ignored) {
                logger.error("error to get[" + field.getName() + "] from [" + staticClass.getName() + "]");
            }
        }
        valueIndexCache.put(staticClass, valueMap);
    }
}