### Conch-Boot

> 企业级快速开发框架集，提供各种组件的模板方法包装，简化使用成本，供参考学习！

##### 本项目基于SpringBoot、SOFAStack、Api-Boot以及企业开发常用Java技术栈

##### 版本：

   ` v1.0.5`

##### 包含：

| 模块                                   | 组件            | 特点描述                                                     |
| -------------------------------------- | --------------- | ------------------------------------------------------------ |
| mongo-conch-boot-starter               | MongoDB         | 开箱即用，支持读写分离                                       |
| redis-conch-boot-starter               | Redis           | 开箱即用，序列化、分布式锁等                                 |
| web-conch-boot-starter                 | Web             | 开箱即用，SofaRPC、Request、Response、Retrofit、异常、邮件等包装 |
| minio-conch-boot-starter               | Minio           | 开箱即用，对象存储（文件、视频、音频等），支持永久链接       |
| anti-scrapy-conch-boot-starter         | Anti-Scrapy     | 开箱即用，反爬虫(屏蔽：恶意请求、渠道刷量等)                 |
| zookeeper-conch-boot-starter           | Zookeeper       | 简单包装curator工具到SpringBoot，操作Zookeeper               |
| dingtalk-conch-boot-starter            | Dingtalk        | 开箱即用，集成钉钉机器人通知API                              |
| mutil-elasticsearch-conch-boot-starter | Elasticsearch   | 开箱即用，多ES数据源支持，基于Spring Elasticsearch and Jest  |
| retrofit-conch-boot-starter            | Retrofit        | 开箱即用，支持类似Feign的注解式Http远程调用支持，提供识别远程转本地调用支持 |

##### 使用方式：
> 1. clone源码
> 2. 本地maven需配置[阿里云maven仓库服务](https://help.aliyun.com/document_detail/102512.html?spm=a2c40.aliyun_maven_repo.0.0.361830547v3oXJ)
> 3. IDEA需安装Lombok插件
> 4. 使用IDEA自带的maven图形界面，选择 `conch-boot` -> `Lifecycle` -> `install`  引入依赖
> 5. 所有开箱组件，对应都有相应的sample使用示例，如果未发现使用示例的Starter，一般整合在`conch-boot-web-sample`中，否则就真的没有示例
> 6. 使用sample需要配置`application.properties`文件中的配置文件

##### 重要：

本项目用于整合学习各路大神优秀的Coding设计，包含本人的一些拙劣代码，如有意见，尽情提Issue。
涉及相关大牛代码部分已保留完整源作者信息，如您确定影响到您的个人权益，请联系我立刻下线，比心！

###### 官方：

`[SpringBoot](https://github.com/spring-projects/spring-boot)`

`[SOFAStack](https://www.sofastack.tech/)`

###### 肩膀：

`[Api-Boot](https://github.com/minbox-projects/api-boot)`

`[tangcent](https://github.com/tangcent)`
