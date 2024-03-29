package com.nabob.conch.security.enhance.handler;

import com.nabob.conch.tools.code.BasicServiceCode;
import com.nabob.conch.web.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义AccessDeniedHandler异常处理器
 * 实现认证异常自定义返回格式化内容到前端
 *
 * @author：恒宇少年 - 于起宇
 * <p>
 * DateTime：2019-04-10 16:09
 * Blog：http://blog.yuqiyu.com
 * WebSite：http://www.jianshu.com/u/092df3f77bca
 * Gitee：https://gitee.com/hengboy
 * GitHub：https://github.com/hengboy
 */
public class ConchDefaultAccessDeniedHandler implements AccessDeniedHandler {
    /**
     * logger instance
     */
    static Logger logger = LoggerFactory.getLogger(ConchDefaultAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        logger.error("ApiBoot Default AccessDeniedHandler.", e);
//        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
//        response.setStatus(HttpStatus.OK.value());
//        // ApiBoot Result
//        ApiBootResult result = ApiBootResult.builder().errorMessage(HttpStatus.FORBIDDEN.getReasonPhrase()).errorCode(String.valueOf(HttpStatus.FORBIDDEN.value())).build();
//        // return json
//        response.getWriter().write(new ObjectMapper().writeValueAsString(result));
        throw new ServiceException(BasicServiceCode.FORBIDDEN);
    }
}
