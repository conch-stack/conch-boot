package ltd.beihu.core.dingtalk.boot;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @Author: zjz
 * @Desc: Dingtalk 客户端
 * @Date: 2019/2/26
 * @Version: V1.0.0
 */
public class DingtalkChatbotClient {

    private HttpClient httpclient = HttpClients.createDefault();
    private String webhook;

    public DingtalkChatbotClient(String webhook) {
        this.webhook = webhook;
    }

    public SendResult send(Message message) throws IOException {
        HttpPost httppost = new HttpPost(webhook);
        httppost.addHeader("Content-Type", "application/json; charset=utf-8");
        StringEntity se = new StringEntity(message.toJsonString(), "utf-8");
        httppost.setEntity(se);
        SendResult sendResult = new SendResult();
        HttpResponse response = this.httpclient.execute(httppost);
        if (response.getStatusLine().getStatusCode() == 200) {
            String result = EntityUtils.toString(response.getEntity());
            JSONObject obj = JSONObject.parseObject(result);
            Integer errcode = obj.getInteger("errcode");
            sendResult.setErrorCode(errcode);
            sendResult.setErrorMsg(obj.getString("errmsg"));
            sendResult.setIsSuccess(errcode.equals(0));
        }
        return sendResult;
    }
}
