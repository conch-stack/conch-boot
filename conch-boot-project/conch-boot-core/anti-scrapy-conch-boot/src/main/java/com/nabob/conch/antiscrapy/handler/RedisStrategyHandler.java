package com.nabob.conch.antiscrapy.handler;

import com.nabob.conch.redis.RedisGenericUtil;

import java.util.concurrent.TimeUnit;

/**
 * @Author: zjz
 * @Desc:
 * @Date: 2019/4/26
 * @Version: V1.0.0
 */
public class RedisStrategyHandler implements StrategyHandler{

    private RedisGenericUtil redisGenericUtil;

    public RedisStrategyHandler(RedisGenericUtil redisGenericUtil) {
        this.redisGenericUtil = redisGenericUtil;
    }

    @Override
    public long handleHour(String target) {
        boolean init = redisGenericUtil.setIfAbsent(target, 1, TimeUnit.HOURS.toMillis(1));
        if (!init) {
            return redisGenericUtil.incr(target, 1);
        }
        return 1;
    }

    @Override
    public long handleDay(String target) {
        boolean init = redisGenericUtil.setIfAbsent(target, 1, TimeUnit.DAYS.toMillis(1));
        if (!init) {
            return redisGenericUtil.incr(target, 1);
        }
        return 1;
    }
}
