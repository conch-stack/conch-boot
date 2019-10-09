package ltd.beihu.core.antiscrapy.boot;

import ltd.beihu.core.antiscrapy.boot.common.SecurityConstants;
import ltd.beihu.core.antiscrapy.boot.strategy.SecurityStrategy;
import ltd.beihu.core.antiscrapy.boot.strategy.SecurityStrategyFactory;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: zjz
 * @Desc: 简易版Security服务
 *              顺序：
 *                  ip->phone->ip&ua
 *                  hour->day
 * @Date: 2019/4/26
 * @Version: V1.0.0
 */
public class AntiSecurityService {

    private SecurityStrategyFactory securityStrategyFactory;

    public AntiSecurityService(SecurityStrategyFactory securityStrategyFactory) {
        this.securityStrategyFactory = securityStrategyFactory;
    }

    /**
     * 校验所有规则
     *      && 运算短路 注意校验顺序
     * @param phoneNum 手机号
     * @param ipAddress IP地址
     * @param userAgent UserAgent
     * @return 是否有权限访问 true 有 ； false 无
     */
    public boolean checkAllSecurity(String ipAddress, String phoneNum, String userAgent) {
        return checkIpSecurity(ipAddress) && checkPhoneSecurity(phoneNum) && checkIpAndUaSecurity(ipAddress, userAgent);
    }

    public boolean checkIpSecurity(String ipAddress) {
        ipAddress = handleBlank(ipAddress, "bi");
        SecurityStrategy ipSecurityStrategy = securityStrategyFactory.create(SecurityConstants.ipSecurityStrategy);
        return ipSecurityStrategy.checkSecurity(ipAddress);
    }

    public boolean checkPhoneSecurity(String phoneNum) {
        phoneNum = handleBlank(phoneNum, "bp");
        SecurityStrategy phoneSecurityStrategy = securityStrategyFactory.create(SecurityConstants.phoneSecurityStrategy);
        return phoneSecurityStrategy.checkSecurity(phoneNum);
    }

    public boolean checkIpAndUaSecurity(String ipAddress, String userAgent) {
        ipAddress = handleBlank(ipAddress, "bi");
        userAgent = handleBlank(userAgent, "bu");
        SecurityStrategy ipAndUaSecurityStrategy = securityStrategyFactory.create(SecurityConstants.ipAndUaSecurityStrategy);
        return ipAndUaSecurityStrategy.checkSecurity(ipAddress, userAgent);
    }

    private String handleBlank(String target, String def) {
        return StringUtils.isBlank(target)?def:target;
    }
}
