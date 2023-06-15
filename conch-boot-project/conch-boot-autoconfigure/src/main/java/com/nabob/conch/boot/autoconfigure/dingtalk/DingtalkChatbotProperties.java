package com.nabob.conch.boot.autoconfigure.dingtalk;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * @Author: zjz
 * @Desc: Dingtalk配置 - WebHook
 * @Date: 2019/10/17
 * @Version: V1.0.0
 */
@ConfigurationProperties(prefix = "conch.dingtalk")
@Validated
public class DingtalkChatbotProperties {

    @NotBlank
    private String webhook;

    public String getWebhook() {
        return webhook;
    }

    public void setWebhook(String webhook) {
        this.webhook = webhook;
    }
}
