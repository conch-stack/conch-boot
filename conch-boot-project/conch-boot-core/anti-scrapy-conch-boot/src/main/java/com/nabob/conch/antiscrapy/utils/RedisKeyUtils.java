package com.nabob.conch.antiscrapy.utils;

/**
 * @Author: zjz
 * @Desc:
 * @Date: 2019/4/26
 * @Version: V1.0.0
 */
public class RedisKeyUtils {

    /*
        anti:h:ip:12333333333
        anti:h:ph:12333333333
        anti:h:iu:12333333333
     */
    public static String generateHourRedisKey(String namespace, String type, String value) {
        return String.format("%s:%s:%s:%s", namespace, "h", type, value);
    }

    /*
        anti:d:ip:12333333333
        anti:d:ph:12333333333
        anti:d:iu:12333333333
     */
    public static String generateDayRedisKey(String namespace, String type, String value) {
        return String.format("%s:%s:%s:%s", namespace, "d", type, value);
    }

}
