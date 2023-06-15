package com.nabob.conch.boot.autoconfigure.dingtalk;

import com.nabob.conch.dingtalk.DingtalkChatbotClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: zjz
 * @Desc: Dingtalk 自动装配
 * @Date: 2019/10/17
 * @Version: V1.0.0
 */
@Configuration
@ConditionalOnClass(DingtalkChatbotClient.class)
@EnableConfigurationProperties(DingtalkChatbotProperties.class)
public class DingtalkChatbotAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DingtalkChatbotClient dingtalkChatbotClient(DingtalkChatbotProperties dingtalkChatbotProperties) {
        return new DingtalkChatbotClient(dingtalkChatbotProperties.getWebhook());
    }
}
