### Beihu-Boot

> 企业级快速开发框架集，提供各种组件的模板方法包装，简化使用成本，供参考学习！

##### 本项目基于SpringBoot、SOFAStack、Api-Boot以及企业开发常用Java技术栈

##### 版本：

   ` v1.0.3`

##### 包含：

| 模块                                  | 组件            | 特点描述                                                     |
| ------------------------------------- | --------------- | ------------------------------------------------------------ |
| mongo-beihu-boot-starter              | MongoDB         | 开箱即用，支持读写分离                                       |
| redis-beihu-boot-starter              | Redis           | 开箱即用，序列化、分布式锁等                                 |
| web-beihu-boot-starter                | Web             | 开箱即用，SofaRPC、Request、Response、Retrofit、异常、邮件等包装 |
| mybatis-enhance-beihu-boot-starter    | Mybatis-Enhance | 开箱即用，支持类jOOQ、JPA。支持代码生成                      |
| mybatis-pageable-beihu-boot-starter   | Mybatis         | 结合Mybatis-Enhance组件，分页查询支持                        |
| datasource-beihu-boot-starter         | MySQL           | 开箱即用，动态数据源支持                                     |
| security-oauth-jwt-beihu-boot-starter | Security+Oauth2 | 开箱即用                                                     |
| minio-beihu-boot-starter              | Minio           | 开箱即用，对象存储（文件、视频、音频等），支持永久链接       |
| quartz-beihu-boot-starter             | Quartz          | 开箱即用，分布式定时任务支持，简化使用                       |
| anti-scrapy-beihu-boot-starter        | Anti-Scrapy     | 开箱即用，反爬虫(屏蔽：恶意请求、渠道刷量等)                 |
| zookeeper-beihu-boot-starter          | Zookeeper       | 简单包装curator工具到SpringBoot，操作Zookeeper             |
| dingtalk-beihu-boot-starter           | Dingtalk        | 开箱即用，集成钉钉机器人通知API                             |

##### 使用方式：
> 1. clone源码
> 2. 本地maven需配置[阿里云maven仓库服务](https://help.aliyun.com/document_detail/102512.html?spm=a2c40.aliyun_maven_repo.0.0.361830547v3oXJ)
> 3. IDEA需安装Lombok插件
> 4. 使用IDEA自带的maven图形界面，选择 `beihu-boot` -> `Lifecycle` -> `install`  引入依赖

##### TODO：

- 分离邮件服务
- Kafka 简单日志 开箱使用
- SOFAStack 深度探索
- 支付宝包装（支付、小程序等）
- 微信开发者 开箱即用
- 验证码 参考：https://github.com/whvcse/EasyCaptcha



##### 重要：

本项目用于整合学习各路大神优秀的Coding设计，包含本人的一些拙劣代码，如有意见，尽情提Issue。
涉及相关大牛代码部分已保留完整源作者信息，如您确定影响到您的个人权益，请联系我立刻下线，比心！

###### 官方：

`[SpringBoot](https://github.com/spring-projects/spring-boot)`

`[SOFAStack](https://www.sofastack.tech/)`

###### 大神：

`[Api-Boot](https://github.com/minbox-projects/api-boot)`

`[tangcent](https://github.com/tangcent)`