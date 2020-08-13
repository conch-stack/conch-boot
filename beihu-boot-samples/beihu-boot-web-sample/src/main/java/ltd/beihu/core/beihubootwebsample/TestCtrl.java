package ltd.beihu.core.beihubootwebsample;

import ltd.beihu.core.tools.code.BasicServiceCode;
import ltd.beihu.core.web.boot.exception.ServiceException;
import ltd.beihu.core.web.boot.response.BasicResponse;
import ltd.beihu.core.web.boot.response.JsonResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: zjz
 * @Desc:
 * @Date: 2019/8/1
 * @Version: V1.0.0
 */
@RestController
public class TestCtrl {

    /**
     * 测试正常web服务
     */
    @RequestMapping("/test1/{value}")
    public String test1(@PathVariable String value) {
        return value;
    }

    /**
     * 测试Json web服务
     */
    @RequestMapping("/test2/{value}")
    public JsonResponse test2(@PathVariable String value) {
        return BasicResponse.success(value);
    }

    /**
     * 测试异常情况
     */
    @RequestMapping("/test3")
    public String test3() {
        throw new ServiceException(BasicServiceCode.FAILED);
    }

}
