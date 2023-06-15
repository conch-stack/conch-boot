package com.nabob.conch.web.response;

/**
 * @Author: zjz
 * @Desc:
 * @Date: 2019/1/7
 * @Version: V1.0.0
 */
public interface JsonResponse {

    int getCode();

    String getMessage();

    String getCodeDescription();
}
