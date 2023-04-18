package ltd.beihu.core.beihubootantiscrapysample;

import ltd.beihu.core.antiscrapy.boot.AntiSecurityService;
import ltd.beihu.core.web.boot.response.BasicResponse;
import ltd.beihu.core.web.boot.response.JsonResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: zjz
 * @Desc: 测试拦截效果
 * @Date: 2019/10/9
 * @Version: V1.0.0
 */
@RestController
@RequestMapping(value = "/anti")
public class TestCtrl {

    @Resource
    private AntiSecurityService antiSecurityService;

    /**
     * 测试单独拦截ip
     */
    @RequestMapping("/test")
    public JsonResponse test() {
        boolean b = antiSecurityService.checkIpSecurity("192.168.16.121");
        return BasicResponse.success(b);
    }

    /**
     * 测试单独拦截phoneNum
     */
    @RequestMapping("/test1")
    public JsonResponse test1() {
        boolean b = antiSecurityService.checkPhoneSecurity("12312312312");
        return BasicResponse.success(b);
    }

    /**
     * 测试拦截ip+ua
     */
    @RequestMapping("/test2")
    public JsonResponse test2() {
        boolean b = antiSecurityService.checkIpAndUaSecurity("192.168.16.121", "android-1");
        return BasicResponse.success(b);
    }

    /**
     * 测试拦截 ip + phoneNum + ua
     *      组合策略
     */
    @RequestMapping("/test3")
    public JsonResponse test3() {
        boolean b = antiSecurityService.checkAllSecurity("12312312313","192.168.16.123", "android-2");
        return BasicResponse.success(b);
    }
}
