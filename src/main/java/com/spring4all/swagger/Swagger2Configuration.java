package com.spring4all.swagger;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.swagger2.configuration.Swagger2DocumentationConfiguration;

/**
 * @author 翟永超
 * Create Date： 2017/9/7.
 * My blog： http://blog.didispace.com
 */
@Configuration
@ConditionalOnProperty(name = "swagger.enabled", matchIfMissing = true)
@Import({
        Swagger2DocumentationConfiguration.class,
        BeanValidatorPluginsConfiguration.class
})
public class Swagger2Configuration {
}
