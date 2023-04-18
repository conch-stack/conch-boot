package com.nabob.conch.tools.code;

/**
 * @Author: zjz
 * @Desc:
 * @Date: 2019/1/7
 * @Version: V1.0.0
 */
public class DefaultServiceCode extends AbstractServiceCode{

    public DefaultServiceCode(int code, String desc) {
        this(code, desc, "请求成功");
    }

    public DefaultServiceCode(int code, String desc, String message) {
        this.code = code;
        this.desc = desc;
        this.message = message;
    }

    private int code;
    private String desc;
    private String message;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String getMesg() {
        return message;
    }

    @Override
    public void setMesg(String mesg) {
        this.message = message;
    }
}
