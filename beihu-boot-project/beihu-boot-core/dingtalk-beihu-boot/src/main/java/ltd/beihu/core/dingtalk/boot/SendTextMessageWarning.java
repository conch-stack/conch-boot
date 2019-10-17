//package ltd.beihu.core.dingtalk.boot;
//
//import com.google.common.collect.Lists;
//import ltd.beihu.geo.core.Job;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.ArrayList;
//
///**
// * @Author: zjz
// * @Desc:
// * @Date: 2019/2/26
// * @Version: V1.0.0
// */
//public class SendTextMessageWarning {
//
//    private static final Logger logger = LoggerFactory.getLogger(SendTextMessageWarning.class);
//
//    public void smsLeft(Job job, String status) throws Exception {
//        DingtalkChatbotClient client = new DingtalkChatbotClient();
//        TextMessage message = new TextMessage("城市位点任务运行提醒：\n" +
//                "\t\tJob任务: " + "\n" +
//                "\t\t\t\t城市：" + job.getCity() + "\n" +
//                "\t\t\t\t纬度区间：" + job.getD_lat() + "\n" +
//                "\t\t\t\t经度区间：" + job.getD_lng() + "\n" +
//                "\t\t\t\t是否包含范围：" + job.getEdge() + "\n" +
//                "\t\t\t\t是否需要重置：" + job.getReset() + "\n" +
//                "\t\t运行状态: " + status);
//        ArrayList<String> atMobiles = Lists.newArrayList();
//        atMobiles.add("15000101536");
//        message.setAtMobiles(atMobiles);
//        SendResult result = client.send("https://oapi.dingtalk.com/robot/send?access_token=2f3089d2986ffe4171e98d49a0b9ed19329e662d17ff6f9a96be791dab0df671", message);
//        logger.info(result.toString());
//    }
//}
