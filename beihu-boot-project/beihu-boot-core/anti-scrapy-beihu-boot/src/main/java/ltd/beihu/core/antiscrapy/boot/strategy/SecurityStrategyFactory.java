package ltd.beihu.core.antiscrapy.boot.strategy;

import ltd.beihu.core.antiscrapy.boot.common.SecurityConstants;

public class SecurityStrategyFactory {

    private SecurityStrategy ipSecurityStrategy;
    private SecurityStrategy phoneSecurityStrategy;
    private SecurityStrategy ipAndUaSecurityStrategy;

    public SecurityStrategyFactory(SecurityStrategy ipSecurityStrategy, SecurityStrategy phoneSecurityStrategy, SecurityStrategy ipAndUaSecurityStrategy) {
        this.ipSecurityStrategy = ipSecurityStrategy;
        this.phoneSecurityStrategy = phoneSecurityStrategy;
        this.ipAndUaSecurityStrategy = ipAndUaSecurityStrategy;
    }

    public SecurityStrategy create(String securityStrategyBeanName) {
        if (SecurityConstants.ipSecurityStrategy.equals(securityStrategyBeanName)) {
            return ipSecurityStrategy;
        }
        if (SecurityConstants.phoneSecurityStrategy.equals(securityStrategyBeanName)) {
            return phoneSecurityStrategy;
        }
        if (SecurityConstants.ipAndUaSecurityStrategy.equals(securityStrategyBeanName)) {
            return ipAndUaSecurityStrategy;
        }
        throw new RuntimeException("Not support this Security Strategy!");
    }

}
