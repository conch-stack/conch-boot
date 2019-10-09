package ltd.beihu.core.boot.autoconfigure.antiscrapy.strategy;

import ltd.beihu.core.antiscrapy.boot.handler.StrategyHandler;
import ltd.beihu.core.antiscrapy.boot.strategy.SecurityStrategy;
import ltd.beihu.core.boot.autoconfigure.antiscrapy.AntiSecurityProperties;

/**
 * @Author: zjz
 * @Desc:
 * @Date: 2019/4/26
 * @Version: V1.0.0
 */
public abstract class AbstractSecurityStrategy implements SecurityStrategy {

    private StrategyHandler strategyHandler;
    private AntiSecurityProperties antiSecurityProperties;

    public AbstractSecurityStrategy(StrategyHandler strategyHandler, AntiSecurityProperties antiSecurityProperties) {
        this.strategyHandler = strategyHandler;
        this.antiSecurityProperties = antiSecurityProperties;
    }

    @Override
    public boolean checkSecurity(String target) {

        /**
         * 存在短路 - 不利于后期黑名单服务
         *      TODO 后期添加黑名单可修改此处
         */
        if (checkHourSecurity(strategyHandler, antiSecurityProperties, target) && checkDaySecurity(strategyHandler, antiSecurityProperties, target)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkSecurity(String target1, String target2) {
        return checkSecurity(target1.concat(target2));
    }

    protected abstract boolean checkHourSecurity(StrategyHandler handler, AntiSecurityProperties properties, String target);
    protected abstract boolean checkDaySecurity(StrategyHandler handler, AntiSecurityProperties properties, String target);
}
