package com.spring4all.swagger;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author 翟永超
 * Create date：2017/8/7.
 * My blog： http://blog.didispace.com
 */
@Configuration
@EnableConfigurationProperties(SwaggerProperties.class)
@Import({
        SwaggerUiConfiguration.class,
        SwaggerAuthorizationConfiguration.class,
        DocketConfiguration.class
})
public class SwaggerAutoConfiguration {

}
