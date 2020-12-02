package com.spring4all.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

/**
 * @author 翟永超
 * Create date：2017/8/7.
 * My blog： http://blog.didispace.com
 */
@Configuration
public class DocketConfiguration {

    private SwaggerProperties swaggerProperties;
    private SwaggerAuthorizationConfiguration swaggerAuthorizationConfiguration;

    public DocketConfiguration(SwaggerProperties swaggerProperties,
                               SwaggerAuthorizationConfiguration swaggerAuthorizationConfiguration) {
        this.swaggerProperties = swaggerProperties;
        this.swaggerAuthorizationConfiguration = swaggerAuthorizationConfiguration;
    }

    @Bean
    public Docket createRestApi() {
        // 文档的基础信息配置
        Docket builder = new Docket(DocumentationType.SWAGGER_2)
                .host(swaggerProperties.getHost())
                .apiInfo(apiInfo(swaggerProperties));

        // 安全相关的配置
        builder.securityContexts(Collections.singletonList(swaggerAuthorizationConfiguration.securityContext()));
        if ("BasicAuth".equalsIgnoreCase(swaggerAuthorizationConfiguration.getType())) {
            builder.securitySchemes(Collections.singletonList(swaggerAuthorizationConfiguration.basicAuth()));
        } else if (!"None".equalsIgnoreCase(swaggerAuthorizationConfiguration.getType())) {
            builder.securitySchemes(Collections.singletonList(swaggerAuthorizationConfiguration.apiKey()));
        }

        // 要忽略的参数类型
        Class<?>[] array = new Class[swaggerProperties.getIgnoredParameterTypes().size()];
        Class<?>[] ignoredParameterTypes = swaggerProperties.getIgnoredParameterTypes().toArray(array);
        builder.ignoredParameterTypes(ignoredParameterTypes);

        // 需要生成文档的接口目标配置
        Docket docket = builder.select()
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))  // 通过扫描包选择接口
//                .paths(paths(swaggerProperties))  // TODO 通过路径匹配选择接口
                .build();

        return docket;
    }

    /**
     * TODO API接口路径选择
     *
     * @param swaggerProperties
     * @return
     */
//    private Predicate paths(SwaggerProperties swaggerProperties) {
    // TODO 原来的方式不能用了
//        return or(
//                regex("/business.*"),
//                regex("/some.*"),
//                regex("/contacts.*"),
//                regex("/pet.*"),
//                regex("/springsRestController.*"),
//                regex("/test.*")
//        );
//        return null;
//    }

    /**
     * API文档基本信息
     *
     * @param swaggerProperties
     * @return
     */
    private ApiInfo apiInfo(SwaggerProperties swaggerProperties) {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .version(swaggerProperties.getVersion())
                .license(swaggerProperties.getLicense())
                .licenseUrl(swaggerProperties.getLicenseUrl())
                .contact(new Contact(swaggerProperties.getContact().getName(),
                        swaggerProperties.getContact().getUrl(),
                        swaggerProperties.getContact().getEmail()))
                .termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl())
                .build();
        return apiInfo;
    }


}
