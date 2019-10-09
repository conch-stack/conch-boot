package ltd.beihu.core.web.boot.exception;

import ltd.beihu.core.tools.code.BasicServiceCode;
import ltd.beihu.core.tools.code.ServiceCode;
import ltd.beihu.core.web.boot.response.BasicResponse;
import ltd.beihu.core.web.boot.response.JsonResponse;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: zjz
 * @Desc:
 * @Date: 2019/1/9
 * @Version: V1.0.0
 */
@Controller
//@RequestMapping("${error.path:/error}")
@RequestMapping("${server.error.path:${error.path:/error}}")
public class DefaultErrorController extends AbstractErrorController {

    private ErrorProperties errorProperties;

    public DefaultErrorController() {
        super(new DefaultErrorAttributes());
        errorProperties = new ErrorProperties();
        errorProperties.setPath("/error");
    }

    public DefaultErrorController(ErrorAttributes errorAttributes, ServerProperties properties) {
        super(errorAttributes == null ? new DefaultErrorAttributes() : errorAttributes);
        if (properties == null) {
            errorProperties = new ErrorProperties();
            errorProperties.setPath("/error");
        } else {
            this.errorProperties = properties.getError();
        }
    }

    @Override
    public String getErrorPath() {
        return this.errorProperties.getPath();
    }

    @RequestMapping
    @ResponseBody
    public JsonResponse error(HttpServletRequest request) {
        HttpStatus httpStatus = getStatus(request);
        ServiceCode serviceError = BasicServiceCode.parse(httpStatus.value());
        return BasicResponse.error(ObjectUtils.defaultIfNull(serviceError, BasicServiceCode.SERVER_ERROR));
    }

    @RequestMapping("/{error}")
    @ResponseBody
    public JsonResponse error(@PathVariable("error") String error) {
        ServiceCode serviceError = EnumUtils.getEnum(BasicServiceCode.class, error);
        return BasicResponse.error(ObjectUtils.defaultIfNull(serviceError, BasicServiceCode.SERVER_ERROR));
    }
}
