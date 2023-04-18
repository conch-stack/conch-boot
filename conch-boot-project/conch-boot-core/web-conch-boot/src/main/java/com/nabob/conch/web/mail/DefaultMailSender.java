package com.nabob.conch.web.mail;

import com.google.common.base.Throwables;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * @Author: zjz
 * @Desc:
 * @Date: 2019/1/8
 * @Version: V1.0.0
 */
public class DefaultMailSender extends AbstarctMailSender {

    public DefaultMailSender(JavaMailSender javaMailSender, String prefix, String sendFrom, String sendTo) {
        super(javaMailSender, prefix, sendFrom, sendTo);
    }

    /**
     * simple use
     */
    public void warn(String message) {
        warn(getSendTo(), message, message);
    }

    /**
     * simple use with exception
     */
    public void warn(String message, Exception e) {
        warn(message, Throwables.getStackTraceAsString(e));
    }

    /**
     * simple use with detail
     */
    public void warn(String message, String detail) {
        warn(getSendTo(), message, detail);
    }

    /**
     * simple use with detail and exception
     */
    public void warn(String message, String detail, Exception e) {
        warn(message, detail.concat(" || ").concat(Throwables.getStackTraceAsString(e)));
    }

    /**
     * base use exp
     */
    public void warn(String email, String message, String detail, Exception e) {
        warn(email, message, detail.concat(" || ").concat(Throwables.getStackTraceAsString(e)));
    }

    /**
     * base use
     */
    public void warn(String email, String message, String detail) {
        sendMail(email, message, detail);
    }

}
