package com.nabob.conch.web.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @Author: zjz
 * @Desc:
 * @Date: 2019/1/7
 * @Version: V1.0.0
 */
public class AbstractJsonResponse implements JsonResponse{

    protected int code;
    protected String message;
    @JsonProperty(value = "code_description")
    protected String codeDescription;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getCodeDescription() {
        return codeDescription;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCodeDescription(String codeDescription) {
        this.codeDescription = codeDescription;
    }
}
