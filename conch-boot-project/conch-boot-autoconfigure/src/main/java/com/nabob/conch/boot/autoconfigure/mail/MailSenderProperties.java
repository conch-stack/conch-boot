package com.nabob.conch.boot.autoconfigure.mail;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: zjz
 * @Desc:
 * @Date: 2019/1/8
 * @Version: V1.0.0
 */
@ConfigurationProperties(prefix = "conch.mail")
public class MailSenderProperties {

    private String prefix;
    private String sendFrom;
    private String sendTo;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSendFrom() {
        return sendFrom;
    }

    public void setSendFrom(String sendFrom) {
        this.sendFrom = sendFrom;
    }

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }
}
