package com.nabob.conch.web.mail;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: zjz
 * @Desc:
 * @Date: 2019/1/8
 * @Version: V1.0.0
 */
public abstract class AbstarctMailSender implements ConchMailSender {

    private static final Logger logger = LoggerFactory.getLogger(AbstarctMailSender.class);

    private JavaMailSender javaMailSender;
    private String prefix;
    private String sendFrom;
    private String sendTo;

    private static final ExecutorService executorService = new ThreadPoolExecutor(
            2, 2, 0L,
            TimeUnit.MINUTES, new LinkedBlockingQueue<>(10),
            new BasicThreadFactory.Builder().daemon(true).namingPattern("ConchMailSender-d").build());

    public AbstarctMailSender(JavaMailSender javaMailSender, String prefix, String sendFrom, String sendTo) {
        this.javaMailSender = javaMailSender;
        this.prefix = prefix;
        this.sendFrom = sendFrom;
        this.sendTo = sendTo;
    }

    public JavaMailSender getJavaMailSender() {
        return javaMailSender;
    }

    @Override
    public void sendMail(String email, String message, String detail) {
        try {
            executorService.execute(() -> {
                doSendMail(email, message, detail);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }
            });
        } catch (Exception e) {
        }
    }

    private void doSendMail(String email, String message, String detail) {
        if (javaMailSender == null || StringUtils.isBlank(sendFrom) ||
                StringUtils.isBlank(email) || StringUtils.isBlank(prefix)) {
            logger.error("=====================【邮件服务异常】初始化失败! =======================");
            return;
        }
        String[] tos = StringUtils.split(email, ",");
        if (tos.length < 1) {
            logger.warn("=====================【邮件服务异常】无发送列表! =======================");
            return;
        }
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(sendFrom);
            simpleMailMessage.setTo(tos);
            simpleMailMessage.setSubject(prefix + " : " + message);
            simpleMailMessage.setText(detail);
            simpleMailMessage.setSentDate(new Date());
            javaMailSender.send(simpleMailMessage);//发送邮件
            logger.info("==========================邮件发送成功==========================");
        } catch (Exception e) {
            logger.error("==========================邮件发送失败==========================", e);
        }
    }

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
