package ltd.beihu.core.securityenhancebeihuboot.domain;

/**
 * @Author: zjz
 * @Desc: 定义鉴权用户的方法，需业务实现
 * @Date: 2019/8/4
 * @Version: V1.0.0
 */
public interface SecurityUser {

    String getUserName();

    String getPassword();

    String getSmsCode();

}
