/*
 * Copyright [2019] [恒宇少年 - 于起宇]
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.nabob.conch.boot.autoconfigure.oauth;

import com.nabob.conch.oauth.ApiBootAuthorizationServerConfiguration;
import com.nabob.conch.oauth.grant.ApiBootOauthTokenGranter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.List;

/**
 * ApiBoot授权服务器配置
 *
 * @author：恒宇少年 - 于起宇
 * <p>
 * DateTime：2019-03-14 16:51
 * Blog：http://blog.yuqiyu.com
 * WebSite：http://www.jianshu.com/u/092df3f77bca
 * Gitee：https://gitee.com/hengboy
 * GitHub：https://github.com/hengboy
 */
public class ApiBootAuthorizationServerAutoConfiguration extends ApiBootAuthorizationServerConfiguration {
    /**
     * 注入属性配置
     */
    protected ApiBootOauthProperties apiBootOauthProperties;

    public ApiBootAuthorizationServerAutoConfiguration(ObjectProvider<List<ApiBootOauthTokenGranter>> objectProvider, ApiBootOauthProperties apiBootOauthProperties) {
        super(objectProvider);
        this.apiBootOauthProperties = apiBootOauthProperties;
    }

    /**
     * 配置jwt生成token的转换
     * 使用自定义Sign Key 进行加密
     *
     * @return Jwt Access Token转换实例
     */
    @Bean
    @ConditionalOnProperty(prefix = ApiBootOauthProperties.API_BOOT_OAUTH_PREFIX, name = "jwt.enable", havingValue = "true")
    public AccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(apiBootOauthProperties.getJwt().getSignKey());
        return converter;
    }

    /**
     * 默认token转换
     * 不配置jwt转换时
     *
     * @return AccessTokenConverter
     */
    @Bean
    @ConditionalOnProperty(prefix = ApiBootOauthProperties.API_BOOT_OAUTH_PREFIX, name = "jwt.enable", havingValue = "false", matchIfMissing = true)
    public AccessTokenConverter defaultAccessTokenConverter() {
        return new DefaultAccessTokenConverter();
    }
}
