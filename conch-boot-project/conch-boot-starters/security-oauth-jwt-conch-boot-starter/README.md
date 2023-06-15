
Oauth2：
    AuthorizationServerConfigurer
        TokenGranter            OAuth2授予令牌
            AuthorizationCodeTokenGranter       授权码模式
            RefreshTokenGranter                 刷新 token 模式
            ImplicitTokenGranter                implicit 模式
            ClientCredentialsTokenGranter       客户端模式
            ResourceOwnerPasswordTokenGranter   密码模式
            实现AbstractTokenGranter             自定义模式
        TokenStore              令牌存储
        AccessTokenConverter    JwtAccessTokenConverter： Jwt Access Token转换实例
        AuthenticationManager   授权管理者：用于整合oauth2的password授权模式
        ClientDetailsService    InMemoryClientDetailsService/JdbcClientDetailsService  Oauth2存储客户端信息

        TokenEnhancer
        DefaultTokenServices
        
Security：
    WebSecurity.IgnoredRequestConfigurer
    CredentialsContainer
    UserDetails
    User
    GrantedAuthority           已授予的权限
    
    UserDetailsService         自定义用户信息：      实现它：代理掉loadUserByUsername()方法，由业务自己实现 参考：ApiBootUserDetailsService
    AuthenticationEntryPoint   端点处理
    AccessDeniedHandler        异常处理             实现它：认证异常自定义返回格式化内容到前端 参考：ApiBootDefaultAccessDeniedHandler
    
    AuthenticationSuccessHandler
    AuthenticationFailureHandler
        @Autowired
        private ObjectMapper mapper;
    
        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                            AuthenticationException exception) throws IOException {
            String message;
            if (exception instanceof UsernameNotFoundException) {
                message = "用户不存在！";
            } else if (exception instanceof BadCredentialsException) {
                message = "用户名或密码错误！";
            } else if (exception instanceof LockedException) {
                message = "用户已被锁定！";
            } else if (exception instanceof DisabledException) {
                message = "用户不可用！";
            } else if (exception instanceof AccountExpiredException) {
                message = "账户已过期！";
            } else if (exception instanceof CredentialsExpiredException) {
                message = "用户密码已过期！";
            } else if (exception instanceof ValidateCodeException || exception instanceof FebsCredentialExcetion) {
                message = exception.getMessage();
            } else {
                message = "认证失败，请联系网站管理员！";
            }
            response.setContentType(FebsConstant.JSON_UTF8);
            response.getWriter().write(mapper.writeValueAsString(ResponseBo.error(message)));
        }
    
     
        
TODO 自定义Filter        
    参考：ApiBootOauthTokenGranter
    
    
    
在WebSecurityConfigurerAdapter的实现类中添加：Cors支持
        @Bean
        public UrlBasedCorsConfigurationSource corsConfigurationSource(){
            CorsConfiguration corsConfiguration = new CorsConfiguration();
            corsConfiguration.addAllowedMethod("*");
            corsConfiguration.addAllowedOrigin("*");
            corsConfiguration.addAllowedHeader("*");
            corsConfiguration.setAllowCredentials(true);
            UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
            corsConfigurationSource.registerCorsConfiguration("/**",corsConfiguration);
            return corsConfigurationSource;
        }    
        
配置自定义用户：
            实现UserDetailsService类
                提供UserDetails对象包括其权限：【AuthorityUtils.commaSeparatedStringToAuthorityList(permissions)】

认证          
1. AbstractAuthenticationProcessingFilter 拦截请求的username和password
2. 将拦截到的username放入：AbstractAuthenticationToken 中，密码可以一起打包成string放进去
3. 将其他HttpServletRequest信息放入：AbstractAuthenticationToken的detail中
        private void setDetails(HttpServletRequest request,
                                SmsCodeAuthenticationToken authRequest) {
            authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
        }
4. 实现AuthenticationProvider的认证方法
        验证用户是否存在
        密码是否正确
        最后给与当前userdetial和对应权限出去 
        
            SmsCodeAuthenticationToken(String mobile) {
                super(null);
                this.principal = mobile;
                setAuthenticated(false);
            }
        
            SmsCodeAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
                super(authorities);
                this.principal = principal;
                super.setAuthenticated(true);
            }
        
        @Override
        public Authentication authenticate(Authentication authentication) {
            SmsCodeAuthenticationToken authenticationToken = (SmsCodeAuthenticationToken) authentication;
            UserDetails userDetails = userDetailService.loadUserByUsername((String) authenticationToken.getPrincipal());
    
            if (userDetails == null)
                throw new InternalAuthenticationServiceException("没有该用户！");
    
            SmsCodeAuthenticationToken authenticationResult = new SmsCodeAuthenticationToken(userDetails, userDetails.getAuthorities());
    
            authenticationResult.setDetails(authenticationToken.getDetails());
    
            return authenticationResult;
        }
5. 实现handler
    成功失败拒绝等    
    
6. 实现SecurityConfigurerAdapter  
    配置拦截器
    apply到  WebSecurityConfigurerAdapter          