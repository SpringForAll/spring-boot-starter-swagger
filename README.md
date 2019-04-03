# 简介

该项目主要利用Spring Boot的自动化配置特性来实现快速的将swagger2引入spring boot应用来生成API文档，简化原生使用swagger2的整合代码。

- 源码地址
  - GitHub：https://github.com/dyc87112/spring-boot-starter-swagger
  - 码云：https://gitee.com/didispace/spring-boot-starter-swagger
- 使用样例：https://github.com/dyc87112/swagger-starter-demo
- 我的博客：http://blog.didispace.com
- 我们社区：http://www.spring4all.com

**小工具一枚，欢迎使用和Star支持，如使用过程中碰到问题，可以提出Issue，我会尽力完善该Starter**

# 版本基础

- Swagger：2.9.2

# 如何使用

在该项目的帮助下，我们的Spring Boot可以轻松的引入swagger2，主需要做下面两个步骤：

- 在`pom.xml`中引入依赖：

```xml
<dependency>
	<groupId>com.spring4all</groupId>
	<artifactId>swagger-spring-boot-starter</artifactId>
	<version>1.9.0.RELEASE</version>
</dependency>
```

**注意：从`1.6.0`开始，我们按Spring Boot官方建议修改了artifactId为`swagger-spring-boot-starter`，1.6.0之前的版本不做修改，依然为使用`spring-boot-starter-swagger` !**

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

默认情况下就能产生所有当前Spring MVC加载的请求映射文档。

# 参数配置

更细致的配置内容参考如下：

## 配置示例

```properties
swagger.enabled=true

swagger.title=spring-boot-starter-swagger
swagger.description=Starter for swagger 2.x
swagger.version=1.4.0.RELEASE
swagger.license=Apache License, Version 2.0
swagger.licenseUrl=https://www.apache.org/licenses/LICENSE-2.0.html
swagger.termsOfServiceUrl=https://github.com/dyc87112/spring-boot-starter-swagger
swagger.contact.name=didi
swagger.contact.url=http://blog.didispace.com
swagger.contact.email=dyc87112@qq.com
swagger.base-package=com.didispace
swagger.base-path=/**
swagger.exclude-path=/error, /ops/**

swagger.globalOperationParameters[0].name=name one
swagger.globalOperationParameters[0].description=some description one
swagger.globalOperationParameters[0].modelRef=string
swagger.globalOperationParameters[0].parameterType=header
swagger.globalOperationParameters[0].required=true
swagger.globalOperationParameters[1].name=name two
swagger.globalOperationParameters[1].description=some description two
swagger.globalOperationParameters[1].modelRef=string
swagger.globalOperationParameters[1].parameterType=body
swagger.globalOperationParameters[1].required=false

// 取消使用默认预定义的响应消息,并使用自定义响应消息
swagger.apply-default-response-messages=false
swagger.global-response-message.get[0].code=401
swagger.global-response-message.get[0].message=401get
swagger.global-response-message.get[1].code=500
swagger.global-response-message.get[1].message=500get
swagger.global-response-message.get[1].modelRef=ERROR
swagger.global-response-message.post[0].code=500
swagger.global-response-message.post[0].message=500post
swagger.global-response-message.post[0].modelRef=ERROR
```

## 配置说明

### 默认配置

```properties
- swagger.enabled=是否启用swagger，默认：true
- swagger.title=标题
- swagger.description=描述
- swagger.version=版本
- swagger.license=许可证
- swagger.licenseUrl=许可证URL
- swagger.termsOfServiceUrl=服务条款URL
- swagger.contact.name=维护人
- swagger.contact.url=维护人URL
- swagger.contact.email=维护人email
- swagger.base-package=swagger扫描的基础包，默认：全扫描
- swagger.base-path=需要处理的基础URL规则，默认：/**
- swagger.exclude-path=需要排除的URL规则，默认：空
- swagger.host=文档的host信息，默认：空
- swagger.globalOperationParameters[0].name=参数名
- swagger.globalOperationParameters[0].description=描述信息
- swagger.globalOperationParameters[0].modelRef=指定参数类型
- swagger.globalOperationParameters[0].parameterType=指定参数存放位置,可选header,query,path,body.form
- swagger.globalOperationParameters[0].required=指定参数是否必传，true,false
```


> `1.3.0.RELEASE`新增：`swagger.host`属性，同时也支持指定docket的配置
>
> `1.4.0.RELEASE`新增：
> - `swagger.enabled`：用于开关swagger的配置
> - `swagger.globalOperationParameters`：用于设置全局的参数，比如：header部分的accessToken等。该参数支持指定docket的配置。

### Path规则说明

`swagger.base-path`和`swagger.exclude-path`使用ANT规则配置。

我们可以使用`swagger.base-path`来指定所有需要生成文档的请求路径基础规则，然后再利用`swagger.exclude-path`来剔除部分我们不需要的。

比如，通常我们可以这样设置：

```properties
management.context-path=/ops

swagger.base-path=/**
swagger.exclude-path=/ops/**, /error
```

上面的设置将解析所有除了`/ops/`开始以及spring boot自带`/error`请求路径。

其中，`exclude-path`可以配合`management.context-path=/ops`设置的spring boot actuator的context-path来排除所有监控端点。

### 分组配置

当我们一个项目的API非常多的时候，我们希望对API文档实现分组。从1.2.0.RELEASE开始，将支持分组配置功能。

![分组功能](https://github.com/dyc87112/spring-boot-starter-swagger/blob/master/images/swagger-group.png)

具体配置内容如下：

```properties
- swagger.docket.<name>.title=标题
- swagger.docket.<name>.description=描述
- swagger.docket.<name>.version=版本
- swagger.docket.<name>.license=许可证
- swagger.docket.<name>.licenseUrl=许可证URL
- swagger.docket.<name>.termsOfServiceUrl=服务条款URL
- swagger.docket.<name>.contact.name=维护人
- swagger.docket.<name>.contact.url=维护人URL
- swagger.docket.<name>.contact.email=维护人email
- swagger.docket.<name>.base-package=swagger扫描的基础包，默认：全扫描
- swagger.docket.<name>.base-path=需要处理的基础URL规则，默认：/**
- swagger.docket.<name>.exclude-path=需要排除的URL规则，默认：空
- swagger.docket.<name>.name=参数名
- swagger.docket.<name>.modelRef=指定参数类型
- swagger.docket.<name>.parameterType=指定参数存放位置,可选header,query,path,body.form
- swagger.docket.<name>.required=true=指定参数是否必传，true,false
- swagger.docket.<name>.globalOperationParameters[0].name=参数名
- swagger.docket.<name>.globalOperationParameters[0].description=描述信息
- swagger.docket.<name>.globalOperationParameters[0].modelRef=指定参数存放位置,可选header,query,path,body.form
- swagger.docket.<name>.globalOperationParameters[0].parameterType=指定参数是否必传，true,false
```

说明：`<name>`为swagger文档的分组名称，同一个项目中可以配置多个分组，用来划分不同的API文档。


**分组配置示例**

```properties
swagger.docket.aaa.title=group-a
swagger.docket.aaa.description=Starter for swagger 2.x
swagger.docket.aaa.version=1.3.0.RELEASE
swagger.docket.aaa.termsOfServiceUrl=https://gitee.com/didispace/spring-boot-starter-swagger
swagger.docket.aaa.contact.name=zhaiyongchao
swagger.docket.aaa.contact.url=http://spring4all.com/
swagger.docket.aaa.contact.email=didi@potatomato.club
swagger.docket.aaa.excludePath=/ops/**
swagger.docket.aaa.globalOperationParameters[0].name=name three
swagger.docket.aaa.globalOperationParameters[0].description=some description three override
swagger.docket.aaa.globalOperationParameters[0].modelRef=string
swagger.docket.aaa.globalOperationParameters[0].parameterType=header

swagger.docket.bbb.title=group-bbb
swagger.docket.bbb.basePackage=com.yonghui
```

说明：默认配置与分组配置可以一起使用。在分组配置中没有配置的内容将使用默认配置替代，所以默认配置可以作为分组配置公共部分属性的配置。`swagger.docket.aaa.globalOperationParameters[0].name`会覆盖同名的全局配置。

### JSR-303校验注解支持（1.5.0 + 支持）

支持对JSR-303校验注解的展示，如下图所示：

![JSR-303校验展示](https://github.com/dyc87112/spring-boot-starter-swagger/blob/master/images/jsr-303.png)

目前共支持以下几个注解：

- `@NotNull`
- `@Max、@Min`
- `@Size`
- `@Pattern`

### 自定义全局响应消息配置（1.6.0 + 支持）

支持 POST,GET,PUT,PATCH,DELETE,HEAD,OPTIONS,TRACE 全局响应消息配置，配置如下

```properties
// 取消使用默认预定义的响应消息,并使用自定义响应消息
swagger.apply-default-response-messages=false
swagger.global-response-message.get[0].code=401
swagger.global-response-message.get[0].message=401get
swagger.global-response-message.get[1].code=500
swagger.global-response-message.get[1].message=500get
swagger.global-response-message.get[1].modelRef=ERROR
swagger.global-response-message.post[0].code=500
swagger.global-response-message.post[0].message=500post
swagger.global-response-message.post[0].modelRef=ERROR
```

### UI功能配置（1.6.0 + 支持）

- 调试按钮的控制(try it out)

```properties
swagger.ui-config.submit-methods=get,delete
```

该参数值为提供调试按钮的HTTP请求类型，多个用,分割。

如果不想开启调试功能，只需要如下设置即可：

```properties
swagger.ui-config.submit-methods=
```

- 其他配置

```properties
# json编辑器
swagger.ui-config.json-editor=false

# 显示请求头
swagger.ui-config.show-request-headers=true

# 页面调试请求的超时时间
swagger.ui-config.request-timeout=5000
```

### ignoredParameterTypes配置（1.6.0 + 支持）

```properties
# 基础配置
swagger.ignored-parameter-types[0]=com.didispace.demo.User
swagger.ignored-parameter-types[1]=com.didispace.demo.Product

# 分组配置
swagger.docket.aaa.ignored-parameter-types[0]=com.didispace.demo.User
swagger.docket.aaa.ignored-parameter-types[1]=com.didispace.demo.Product
```

> 该参数作用：
> Q. Infinite loop when springfox tries to determine schema for objects with nested/complex constraints?
> A. If you have recursively defined objects, I would try and see if providing an alternate type might work or perhaps even ignoring the offending classes e.g. order using the docket. ignoredParameterTypes(Order.class). This is usually found in Hibernate domain objects that have bidirectional dependencies on other objects.

### Authorization 鉴权配置 (1.7.0 + 支持)

- 新增 Authorization 配置项

```properties
# 鉴权策略ID，对应 SecurityReferences ID
swagger.authorization.name=Authorization

# 鉴权策略，可选 ApiKey | BasicAuth | None，默认ApiKey
swagger.authorization.type=ApiKey

# 鉴权传递的Header参数
swagger.authorization.key-name=token

# 需要开启鉴权URL的正则, 默认^.*$匹配所有URL
swagger.authorization.auth-regex=^.*$
```

备注：目前支持`ApiKey` | `BasicAuth`鉴权模式，`None`除消鉴权模式，默认ApiKey，后续添加`Oauth2`支持

**使用须知**

> 1. 默认已经在全局开启了`global`的SecurityReferences，无需配置任何参数就可以使用；
> 2. 全局鉴权的范围在可以通过以上参数`auth-regex`进行正则表达式匹配控制；
> 3. 除了全局开启外，还可以手动通过注解在RestController上进行定义鉴权，使用方式如下：

```java
// 其中的ID Authorization 即为配置项 swagger.authorization.name，详细请关注后面的配置代码
@ApiOperation(value = "Hello World", authorizations = {@Authorization(value = "Authorization")})
@RequestMapping(value = "/hello", method = RequestMethod.GET)
String hello();
```

**关于如何配置实现鉴权，请关注以下code：**

```java
/**
 * 配置基于 ApiKey 的鉴权对象
 *
 * @return
 */
private ApiKey apiKey() {
    return new ApiKey(swaggerProperties().getAuthorization().getName(),
            swaggerProperties().getAuthorization().getKeyName(),
            ApiKeyVehicle.HEADER.getValue());
}

/**
 * 配置默认的全局鉴权策略的开关，以及通过正则表达式进行匹配；默认 ^.*$ 匹配所有URL
 * 其中 securityReferences 为配置启用的鉴权策略
 *
 * @return
 */
private SecurityContext securityContext() {
    return SecurityContext.builder()
            .securityReferences(defaultAuth())
            .forPaths(PathSelectors.regex(swaggerProperties().getAuthorization().getAuthRegex()))
            .build();
}

/**
 * 配置默认的全局鉴权策略；其中返回的 SecurityReference 中，reference 即为ApiKey对象里面的name，保持一致才能开启全局鉴权
 *
 * @return
 */
private List<SecurityReference> defaultAuth() {
    AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
    authorizationScopes[0] = authorizationScope;
    return Collections.singletonList(SecurityReference.builder()
            .reference(swaggerProperties().getAuthorization().getName())
            .scopes(authorizationScopes).build());
}
```

## 贡献者

- [程序猿DD-翟永超](https://github.com/dyc87112/)
- [小火](https://renlulu.github.io/)
- [泥瓦匠BYSocket](https://github.com/JeffLi1993)
- [LarryKoo-古拉里](https://github.com/gumutianqi)
