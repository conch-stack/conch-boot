package com.nabob.conch.boot.autoconfigure.antiscrapy.strategy;

import com.nabob.conch.antiscrapy.handler.StrategyHandler;
import com.nabob.conch.antiscrapy.utils.RedisKeyUtils;
import com.nabob.conch.boot.autoconfigure.antiscrapy.AntiSecurityProperties;

/**
 * @Author: zjz
 * @Desc:
 * @Date: 2019/4/26
 * @Version: V1.0.0
 */
public class IpSecurityStrategy extends AbstractSecurityStrategy {

    private static final String type = "ip";

    public IpSecurityStrategy(StrategyHandler strategyHandler, AntiSecurityProperties antiSecurityProperties) {
        super(strategyHandler, antiSecurityProperties);
    }

    @Override
    public boolean checkHourSecurity(StrategyHandler handler, AntiSecurityProperties properties, String target) {
        return handler.handleHour(RedisKeyUtils.generateHourRedisKey(properties.getNamespace(), type, target)) <= properties.getHour().getIp();
    }

    @Override
    protected boolean checkDaySecurity(StrategyHandler handler, AntiSecurityProperties properties, String target) {
        return handler.handleDay(RedisKeyUtils.generateDayRedisKey(properties.getNamespace(), type, target)) <= properties.getDay().getIp();
    }

}
