package ltd.beihu.core.beihubootdingtalksample;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import ltd.beihu.core.dingtalk.boot.DingtalkChatbotClient;
import ltd.beihu.core.dingtalk.boot.SendResult;
import ltd.beihu.core.dingtalk.boot.TextMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;

/**
 * @Author: zjz
 * @Desc: DingTalk Demo
 * @Date: 2019/2/26
 * @Version: V1.0.0
 */
@Service
@Slf4j
public class SendTextMessageWarning {

    @Resource
    private DingtalkChatbotClient dingtalkChatbotClient;

    /**
     * 发送钉钉机器人信息
     */
    public void dingtalkMessage(String message) throws Exception {
        // 自定义Message内容
        TextMessage textMessage = new TextMessage("城市位点任务运行提醒：\n" +
                "\t\tJob任务: " + "\n" +
                "\t\t\t\t城市：\n" +
                "\t\t\t\t纬度区间：\n" +
                "\t\t\t\t经度区间：\n" +
                "\t\t\t\t是否包含范围：\n" +
                "\t\t\t\t是否需要重置：\n" +
                "\t\t运行状态: ");
        ArrayList<String> atMobiles = Lists.newArrayList();
        atMobiles.add("15000xxxxx");
        textMessage.setAtMobiles(atMobiles);
        SendResult result = dingtalkChatbotClient.send(textMessage);
        log.info(result.toString());
    }
}
