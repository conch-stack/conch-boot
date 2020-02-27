在之前的版本我们都是使用 @Configuration、 @ConfigurationProperties这两个注解来进行配置映射，
从 SpringBoot2.2.1.RELEASE版本开始我们不再需要添加 @Configuration，
只要通过 @ConfigurationPropertiesScan结合 @ConfigurationProperties搭配使用即可，
会自动扫描指定 package下的属性配置类进行绑定。

在属性配置类上添加 @ConstructorBinding注解，
即可实现构造函数的方式进行对应字段设置值，我们只需要把绑定赋值的参数通过构造函数的方式定义。