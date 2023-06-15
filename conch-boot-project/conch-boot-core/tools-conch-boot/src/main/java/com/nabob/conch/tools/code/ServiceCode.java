package com.nabob.conch.tools.code;

/**
 * @Author: zjz
 * @Desc:
 * @Date: 2019/1/7
 * @Version: V1.0.0
 */
public interface ServiceCode {

    public int getCode();

    public String getDesc();

    default Object getData() {
        return null;
    }

    void setCode(int code);

    void setDesc(String desc);

    String getMesg();

    void setMesg(String mesg);

    default String name() {
        return "UnKnownError";
    }
}

