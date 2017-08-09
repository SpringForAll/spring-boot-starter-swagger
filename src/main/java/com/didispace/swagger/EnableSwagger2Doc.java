package com.didispace.swagger;

import org.springframework.context.annotation.Import;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.lang.annotation.*;

/**
 * @author 翟永超
 * Create date ：2017/8/7.
 * My blog： http://blog.didispace.com
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableSwagger2
@Import(SwaggerAutoConfiguration.class)
public @interface EnableSwagger2Doc {


}
