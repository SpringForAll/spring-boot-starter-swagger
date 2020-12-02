package com.spring4all.swagger;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;

/**
 * @author 翟永超
 * Create date：2017/8/7.
 * My blog： http://blog.didispace.com
 */
@Configuration
@EnableConfigurationProperties(SwaggerUiProperties.class)
public class SwaggerUiConfiguration {

    @Bean
    public UiConfiguration uiConfiguration(SwaggerUiProperties swaggerUiProperties) {
        return UiConfigurationBuilder.builder()
                .deepLinking(swaggerUiProperties.getDeepLinking())
                .defaultModelExpandDepth(swaggerUiProperties.getDefaultModelExpandDepth())
                .defaultModelRendering(swaggerUiProperties.getDefaultModelRendering())
                .defaultModelsExpandDepth(swaggerUiProperties.getDefaultModelsExpandDepth())
                .displayOperationId(swaggerUiProperties.getDisplayOperationId())
                .displayRequestDuration(swaggerUiProperties.getDisplayRequestDuration())
                .docExpansion(swaggerUiProperties.getDocExpansion())
                .maxDisplayedTags(swaggerUiProperties.getMaxDisplayedTags())
                .operationsSorter(swaggerUiProperties.getOperationsSorter())
                .showExtensions(swaggerUiProperties.getShowExtensions())
                .tagsSorter(swaggerUiProperties.getTagsSorter())
                .validatorUrl(swaggerUiProperties.getValidatorUrl())
                .build();
    }

}
