package com.nabob.conch.boot.quartzsample.jobs;

import com.alipay.sofa.rpc.common.json.JSON;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 任务定义示例
 * 与Quartz使用方法一致，ApiBoot只是在原生基础上进行扩展，不影响原生使用
 * 继承QuartzJobBean抽象类后会在项目启动时会自动加入Spring IOC
 */
public class DemoJob extends QuartzJobBean {
    /**
     * logger instance
     */
    static Logger logger = LoggerFactory.getLogger(DemoJob.class);

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("定时任务Job Key ： {}", context.getJobDetail().getKey());
        logger.info("定时任务执行时所携带的参数：{}", JSON.toJSONString(context.getJobDetail().getJobDataMap()));
        //...处理逻辑
//        context.getJobDetail().getJobDataMap().getString("")
    }
}
