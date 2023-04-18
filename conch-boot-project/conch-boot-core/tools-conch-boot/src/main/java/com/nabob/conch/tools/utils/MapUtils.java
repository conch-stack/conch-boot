package com.nabob.conch.tools.utils;

import com.nabob.conch.tools.cast.CasterUtils;

import java.util.Map;

public class MapUtils {
    public static <T> T get(Map map, String key, Class<T> clz) {
        return get(map, key, clz, null);
    }

    public static <T> T get(Map map, String key, Class<T> clz, T defaultVal) {
        return CasterUtils.cast(map.get(key), clz, defaultVal);
    }

}
