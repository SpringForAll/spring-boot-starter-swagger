package com.spring4all.swagger;

import com.google.common.base.Predicates;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;

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

        // 设置全局参数
        if (swaggerProperties.getGlobalOperationParameters() != null) {
            builder.globalRequestParameters(globalRequestParameters(swaggerProperties));
        }

        // 需要生成文档的接口目标配置
        Docket docket = builder.select()
                // 通过扫描包选择接口
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))
                // 通过路径匹配选择接口
                .paths(paths(swaggerProperties))
                .build();

        return docket;
    }

    /**
     * 全局请求参数
     *
     * @param swaggerProperties {@link SwaggerProperties}
     * @return RequestParameter {@link RequestParameter}
     */
    private List<RequestParameter> globalRequestParameters(SwaggerProperties swaggerProperties) {
        return swaggerProperties.getGlobalOperationParameters().stream().map(param -> new RequestParameterBuilder()
                .name(param.getName())
                .description(param.getDescription())
                .in(param.getParameterType())
                .required(param.getRequired())
                .query(q -> q.defaultValue(param.getModelRef()))
                .query(q -> q.model(m -> m.scalarModel(ScalarType.STRING)))
                .build()).collect(Collectors.toList());
    }

    /**
     * API接口路径选择
     *
     * @param swaggerProperties
     * @return
     */

    private Predicate paths(SwaggerProperties swaggerProperties) {
        // base-path处理
        // 当没有配置任何path的时候，解析/**
        if (swaggerProperties.getBasePath().isEmpty()) {
            swaggerProperties.getBasePath().add("/**");
        }
        List<com.google.common.base.Predicate<String>> basePath = new ArrayList<>();
        for (String path : swaggerProperties.getBasePath()) {
            basePath.add(PathSelectors.ant(path));
        }

        // exclude-path处理
        List<com.google.common.base.Predicate<String>> excludePath = new ArrayList<>();
        for (String path : swaggerProperties.getExcludePath()) {
            excludePath.add(PathSelectors.ant(path));
        }

        return Predicates.and(
                Predicates.not(Predicates.or(excludePath)),
                Predicates.or(basePath)
        );
    }

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
