package ltd.beihu.core.boot.autoconfigure.antiscrapy.strategy;

import ltd.beihu.core.antiscrapy.boot.handler.StrategyHandler;
import ltd.beihu.core.antiscrapy.boot.utils.RedisKeyUtils;
import ltd.beihu.core.boot.autoconfigure.antiscrapy.AntiSecurityProperties;

/**
 * @Author: zjz
 * @Desc:
 * @Date: 2019/4/26
 * @Version: V1.0.0
 */
public class PhoneSecurityStrategy extends AbstractSecurityStrategy {

    private static final String type = "ph";

    public PhoneSecurityStrategy(StrategyHandler strategyHandler, AntiSecurityProperties antiSecurityProperties) {
        super(strategyHandler, antiSecurityProperties);
    }

    @Override
    public boolean checkHourSecurity(StrategyHandler handler, AntiSecurityProperties properties, String target) {
        return handler.handleHour(RedisKeyUtils.generateHourRedisKey(properties.getNamespace(), type, target)) <= properties.getHour().getPhone();
    }

    @Override
    protected boolean checkDaySecurity(StrategyHandler handler, AntiSecurityProperties properties, String target) {
        return handler.handleDay(RedisKeyUtils.generateDayRedisKey(properties.getNamespace(), type, target)) <= properties.getDay().getPhone();
    }
}
