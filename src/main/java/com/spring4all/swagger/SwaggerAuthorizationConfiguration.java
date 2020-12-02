package com.spring4all.swagger;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.swagger.web.ApiKeyVehicle;

import java.util.Collections;
import java.util.List;

/**
 * securitySchemes 支持方式之一 ApiKey
 *
 * @author 翟永超
 * Create date：2020/2/2.
 * My blog： http://blog.didispace.com
 */
@Configuration
@EnableConfigurationProperties(SwaggerAuthorizationProperties.class)
public class SwaggerAuthorizationConfiguration {

    public SwaggerAuthorizationProperties swaggerAuthorizationProperties;

    public SwaggerAuthorizationConfiguration(SwaggerAuthorizationProperties swaggerAuthorizationProperties) {
        this.swaggerAuthorizationProperties = swaggerAuthorizationProperties;
    }

    /**
     * 配置默认的全局鉴权策略的开关，以及通过正则表达式进行匹配；默认 ^.*$ 匹配所有URL
     * 其中 securityReferences 为配置启用的鉴权策略
     *
     * @return
     */
    public SecurityContext securityContext() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> defaultAuth = Collections.singletonList(SecurityReference.builder()
                .reference(swaggerAuthorizationProperties.getName())
                .scopes(authorizationScopes).build());

        return SecurityContext.builder()
                .securityReferences(defaultAuth)
                .forPaths(PathSelectors.regex(swaggerAuthorizationProperties.getAuthRegex()))
                .build();
    }

    /**
     * 配置基于 ApiKey 的鉴权对象
     *
     * @return
     */
    public ApiKey apiKey() {
        return new ApiKey(swaggerAuthorizationProperties.getName(),
                swaggerAuthorizationProperties.getKeyName(),
                ApiKeyVehicle.HEADER.getValue());
    }

    /**
     * 配置基于 BasicAuth 的鉴权对象
     *
     * @return
     */
    public BasicAuth basicAuth() {
        return new BasicAuth(swaggerAuthorizationProperties.getName());
    }

    public String getType() {
        return swaggerAuthorizationProperties.getType();
    }


}
