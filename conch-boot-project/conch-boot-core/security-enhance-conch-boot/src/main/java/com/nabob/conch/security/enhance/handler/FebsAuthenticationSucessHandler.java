package com.nabob.conch.security.enhance.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录成功处理器
 */
public class FebsAuthenticationSucessHandler implements AuthenticationSuccessHandler {

//    private ObjectMapper mapper = new ObjectMapper();
//
//    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
//
//    private SessionRegistry sessionRegistry;
//
//    @Autowired
//    private FebsSecurityProperties febsSecurityProperties;
//
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
//        WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
//        String remoteAddress = details.getRemoteAddress();
//
//        LoginType loginType = LoginType.normal;
//        Object principal = authentication.getPrincipal();
//        if (principal instanceof FebsUserDetails) {
//            FebsUserDetails userDetails = (FebsUserDetails) principal;
//            userDetails.setRemoteAddress(remoteAddress);
//            loginType = userDetails.getLoginType();
//        }
//        if (principal instanceof FebsSocialUserDetails) {
//            FebsSocialUserDetails userDetails = (FebsSocialUserDetails) principal;
//            userDetails.setRemoteAddress(remoteAddress);
//            loginType = userDetails.getLoginType();
//        }
//
//        // 解决第三方登录在 session 并发控制设置不生效的问题
//        if (!LoginType.normal.equals(loginType)) {
//            String sessionId = details.getSessionId();
//            sessionRegistry.removeSessionInformation(sessionId);
//            sessionRegistry.registerNewSession(sessionId, authentication.getPrincipal());
//
//            ConcurrentSessionControlAuthenticationStrategy authenticationStrategy = new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry);
//
//            // 手动设置最大并发登录数量，因为在 sessionManagement 中设置的 maximumSessions
//            // 只对账号密码登录的方式生效 =。=
//            authenticationStrategy.setMaximumSessions(febsSecurityProperties.getSession().getMaximumSessions());
//            authenticationStrategy.onAuthentication(authentication, request, response);
//
//            // 社交账户登录成功后直接 重定向到主页
//            if (LoginType.social.equals(loginType))
//                redirectStrategy.sendRedirect(request, response, febsSecurityProperties.getIndexUrl());
//        }
//        response.setContentType(FebsConstant.JSON_UTF8);
//        response.getWriter().write(mapper.writeValueAsString(ResponseBo.ok()));
    }

//    public SessionRegistry getSessionRegistry() {
//        return sessionRegistry;
//    }
//
//    public void setSessionRegistry(SessionRegistry sessionRegistry) {
//        this.sessionRegistry = sessionRegistry;
//    }
}
