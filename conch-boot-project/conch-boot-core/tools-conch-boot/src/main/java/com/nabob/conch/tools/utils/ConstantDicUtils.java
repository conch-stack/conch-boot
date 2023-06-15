package com.nabob.conch.tools.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.nabob.conch.tools.properties.PropertiesReader;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static java.util.concurrent.TimeUnit.MINUTES;

public class ConstantDicUtils {

    private static Cache<String, String> cache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterWrite(10, MINUTES)
            .build();

    public static String toName(Object value, Class constantCls) {
        String field = StaticFieldUtils.valueToNameSafety(constantCls, value);
        if (field == null) {
            return null;
        }
        String key = constantCls.getName() + "." + field;
        String name = cache.getIfPresent(key);
        if (name == null) {
            if (StringUtils.contains(key, '$')) {
                key = StringUtils.replace(key, "$", ".");
            }

            while (name == null && key.length() > 0) {
                key = StringUtils.substringAfter(key, ".");
                name = dics.get(key);
            }
            if (name != null) {
                cache.put(key, name);
            }
        }

        return name;
    }

    private static Map<String, String> dics;

    private void init() {
        dics = new HashMap<>();

        new PropertiesReader()
                .load(getClass(), "constant-dic.properties")
                .load(getClass(), "config/constant-dic.properties")
                .write(dics);

    }
}
