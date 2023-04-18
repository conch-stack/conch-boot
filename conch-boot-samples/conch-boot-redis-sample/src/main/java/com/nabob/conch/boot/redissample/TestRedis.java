package com.nabob.conch.boot.redissample;

import com.nabob.conch.redis.RedisGenericUtil;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @Author: zjz
 * @Desc:
 * @Date: 2019/7/31
 * @Version: V1.0.0
 */
@Service
public class TestRedis {

    @Resource
    private RedisGenericUtil redisGenericUtil;

    @PostConstruct
    public void test() {

        redisGenericUtil.set("test", "这是一个测试");


    }

}
