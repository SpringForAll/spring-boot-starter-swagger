package org.sidao.swagger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author fenglei
 */
@Configuration
public class SwaggerConfig implements WebMvcConfigurer {

    @Value("${swagger.enabled:true}")
    private boolean swaggerEnabled;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (!swaggerEnabled) {
            registry.addInterceptor(new HandlerInterceptor() {
                @Override
                public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                    response.sendRedirect("/");
                    return false;
                }
            }).addPathPatterns("/swagger-ui.html");
        }
    }
}
