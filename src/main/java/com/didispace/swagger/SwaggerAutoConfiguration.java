package com.didispace.swagger;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 翟永超
 * @create 2017/8/7.
 * @blog http://blog.didispace.com
 */
@Configuration
public class SwaggerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SwaggerProperties swaggerProperties() {
        return new SwaggerProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    public Docket createRestApi(SwaggerProperties swaggerProperties) {
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

        // 当没有配置任何path的时候，解析/**
        if(swaggerProperties.getBasePath().isEmpty()) {
            swaggerProperties.getBasePath().add("/**");
        }

        List<Predicate<String>> basePath = new ArrayList();
        for(String path : swaggerProperties.getBasePath()) {
            basePath.add(PathSelectors.ant(path));
        }

        List<Predicate<String>> excludePath = new ArrayList();
        for(String path : swaggerProperties.getExcludePath()) {
            excludePath.add(PathSelectors.ant(path));
        }

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .select()
                .paths(
                        Predicates.and(
                                Predicates.not(Predicates.or(excludePath)),
                                Predicates.or(basePath)
                        )
                )
                .build();
    }

}
