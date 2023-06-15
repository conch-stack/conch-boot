在之前的版本我们都是使用 @Configuration、 @ConfigurationProperties这两个注解来进行配置映射，
从 SpringBoot2.2.1.RELEASE版本开始我们不再需要添加 @Configuration，
只要通过 @ConfigurationPropertiesScan结合 @ConfigurationProperties搭配使用即可，
会自动扫描指定 package下的属性配置类进行绑定。

在属性配置类上添加 @ConstructorBinding注解，
即可实现构造函数的方式进行对应字段设置值，我们只需要把绑定赋值的参数通过构造函数的方式定义。

- 分离邮件服务
- Kafka 简单日志 开箱使用
- SOFAStack 深度探索
- 支付宝包装（支付、小程序等）
- 微信开发者 开箱即用
- 验证码
    - https://github.com/whvcse/EasyCaptcha
- JVM监控支持
- JVM研究
    - https://github.com/alibaba/metrics
    - https://github.com/alibaba/jvm-sandbox
    - https://github.com/alibaba/jvm-sandbox-repeater
- 事件驱动
    - https://gitee.com/zkpursuit/kaka-notice-lib
- 任务调度
    - https://github.com/apache/incubator-dolphinscheduler
    - https://github.com/xuxueli/xxl-job/
- 插件开发模式
    - DataX or https://github.com/alibaba/jvm-sandbox-repeater
- SQL解析&优化
    - https://tech.meituan.com/2018/05/20/sql-parser-used-in-mtdp.html
- 借鉴自研MyBatis-plus