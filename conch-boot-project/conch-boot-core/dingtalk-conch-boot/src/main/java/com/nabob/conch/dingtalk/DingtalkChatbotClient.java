package com.nabob.conch.dingtalk;

import com.alibaba.fastjson.JSONObject;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;

import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @Author: zjz
 * @Desc: Dingtalk 客户端
 * @Date: 2019/2/26
 * @Version: V1.0.0
 */
public class DingtalkChatbotClient implements Closeable {

    private final CloseableHttpClient httpclient = HttpClients.createDefault();
    private final String webhook;

    public DingtalkChatbotClient(String webhook) {
        this.webhook = webhook;
    }

    public SendResult send(Message message) throws IOException {
        ClassicHttpRequest httpPost = ClassicRequestBuilder.post(webhook)
                .setHeader("Content-Type", "application/json; charset=utf-8")
                .setEntity(new StringEntity(message.toJsonString(), StandardCharsets.UTF_8))
                .build();

        return httpclient.execute(httpPost, response -> {
            SendResult sendResult = new SendResult();
            if (response.getCode() == 200) {
                final HttpEntity entity2 = response.getEntity();
                EntityUtils.consume(entity2);

                String result = EntityUtils.toString(response.getEntity());
                JSONObject obj = JSONObject.parseObject(result);
                Integer errcode = obj.getInteger("errcode");
                sendResult.setErrorCode(errcode);
                sendResult.setErrorMsg(obj.getString("errmsg"));
                sendResult.setIsSuccess(errcode.equals(0));
            }
            return sendResult;
        });
    }

    @Override
    public void close() throws IOException {
        httpclient.close();
    }
}
