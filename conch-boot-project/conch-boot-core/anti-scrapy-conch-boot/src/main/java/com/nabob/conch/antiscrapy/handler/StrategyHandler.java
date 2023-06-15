package com.nabob.conch.antiscrapy.handler;

/**
 * @Author: zjz
 * @Desc:
 * @Date: 2019/4/26
 * @Version: V1.0.0
 */
public interface StrategyHandler {

    long handleHour(String target);

    long handleDay(String target);
}
