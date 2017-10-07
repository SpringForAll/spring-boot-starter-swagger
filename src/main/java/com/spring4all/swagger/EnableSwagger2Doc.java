package com.spring4all.swagger;

import org.springframework.context.annotation.Import;

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
@Import({SwaggerAutoConfiguration.class})
public @interface EnableSwagger2Doc {


}
