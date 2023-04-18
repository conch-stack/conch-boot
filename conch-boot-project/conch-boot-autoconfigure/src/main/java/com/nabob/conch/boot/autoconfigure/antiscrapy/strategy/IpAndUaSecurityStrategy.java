package com.nabob.conch.boot.autoconfigure.antiscrapy.strategy;

import com.nabob.conch.antiscrapy.handler.StrategyHandler;
import com.nabob.conch.antiscrapy.utils.RedisKeyUtils;
import com.nabob.conch.boot.autoconfigure.antiscrapy.AntiSecurityProperties;
import com.nabob.conch.tools.utils.HashHelper;

/**
 * @Author: zjz
 * @Desc:
 * @Date: 2019/4/26
 * @Versin: V1.0.0
 */
public class IpAndUaSecurityStrategy extends AbstractSecurityStrategy {

    private static final String type = "iu";

    public IpAndUaSecurityStrategy(StrategyHandler strategyHandler, AntiSecurityProperties antiSecurityProperties) {
        super(strategyHandler, antiSecurityProperties);
    }

    @Override
    protected boolean checkHourSecurity(StrategyHandler handler, AntiSecurityProperties properties, String target) {
        return handler.handleHour(RedisKeyUtils.generateHourRedisKey(properties.getNamespace(), type, HashHelper.hashOf(target))) <= properties.getHour().getIpAndUa();
    }

    @Override
    protected boolean checkDaySecurity(StrategyHandler handler, AntiSecurityProperties properties, String target) {
        return handler.handleDay(RedisKeyUtils.generateDayRedisKey(properties.getNamespace(), type, HashHelper.hashOf(target))) <= properties.getDay().getIpAndUa();
    }
}
