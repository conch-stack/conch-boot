package com.nabob.conch.boot.autoconfigure.mail;

import com.nabob.conch.web.mail.ConchMailSender;
import com.nabob.conch.web.mail.DefaultMailSender;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * @Author: zjz
 * @Desc: 邮件自动配置
 * @Date: 2019/1/7
 * @Version: V1.0.0
 */
@Configuration
@ConditionalOnClass(ConchMailSender.class)
@EnableConfigurationProperties(MailSenderProperties.class)
@AutoConfigureBefore(MailSenderAutoConfiguration.class)
@ComponentScan("com.nabob.conch.web.exception")
public class MailAutoConfiguration {

    @Bean(name = "defaultMailSender")
    @ConditionalOnMissingBean(name = "defaultMailSender")
    public DefaultMailSender defaultMailSender(JavaMailSender mailSender, MailSenderProperties mailSenderProperties) {
        return new DefaultMailSender(mailSender, mailSenderProperties.getPrefix(), mailSenderProperties.getSendFrom(), mailSenderProperties.getSendTo());
    }
}
