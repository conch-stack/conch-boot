package com.nabob.conch.tools.image;

import java.util.Objects;

/**
 * code定义
 *
 * @author layker
 * @date 2018/4/25 上午10:41
 */
public class Code {
    private String code;
    private String imgBase64Str;


    @Override
    public String toString() {
        return "Code{" +
                "code='" + code + '\'' +
                ", imgBase64Str='" + imgBase64Str + '\'' +
                '}';
    }

    public Code() {
    }

    public Code(String code, String imgBase64Str) {
        this.code = code;
        this.imgBase64Str = imgBase64Str;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Code code1 = (Code) o;
        return Objects.equals(code, code1.code);
    }

    @Override
    public int hashCode() {

        return Objects.hash(code);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImgBase64Str() {
        return imgBase64Str;
    }

    public void setImgBase64Str(String imgBase64Str) {
        this.imgBase64Str = imgBase64Str;
    }
}
