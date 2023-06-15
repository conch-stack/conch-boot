package com.nabob.conch.web.mail;

/**
 * @Author: zjz
 * @Desc: 邮件服务
 * @Date: 2019/1/7
 * @Version: V1.0.0
 */
public interface ConchMailSender {

    void sendMail(String email, String message, String detail);

}
