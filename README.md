# spring-boot-starter-swagger

该项目主要利用Spring Boot的自动化配置特性来实现快速的将swagger2引入spring boot应用来生成API文档。

# 如何使用

在该项目的帮助下，我们的Spring Boot可以轻松的引入swagger2，主需要做下面两个步骤：

- 在`pom.xml`中引入依赖：

```xml
<dependency>
	<groupId>com.didispace</groupId>
	<artifactId>spring-boot-starter-swagger</artifactId>
	<version>1.0.0.RELEASE</version>
</dependency>
```

- 在应用主类中增加`@EnableSwagger2Doc`注解

```java
@EnableSwagger2Doc
@SpringBootApplication
public class Bootstrap {

    public static void main(String[] args) {
        SpringApplication.run(Bootstrap.class, args);
    }

}
```

默认情况下就能产生所有Spring MVC加载的请求文档了。

# 参数配置

更细致的配置文档内容，可以参考如下参数：

```properties
swagger.title=标题
swagger.description=描述
swagger.version=版本
swagger.license=许可证
swagger.licenseUrl=许可证URL
swagger.termsOfServiceUrl=服务条款URL
swagger.contact.name=维护人
swagger.contact.url=维护人URL
swagger.contact.email=维护人email
swagger.base-path=产生文档的基础URL规则，默认：/**
swagger.exclude-path=需要排除的URL规则，默认为空
```

`swagger.base-path`和`swagger.exclude-path`使用ANT规则配置