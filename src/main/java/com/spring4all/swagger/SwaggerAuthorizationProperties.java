package com.spring4all.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import springfox.documentation.swagger.web.DocExpansion;
import springfox.documentation.swagger.web.ModelRendering;
import springfox.documentation.swagger.web.OperationsSorter;
import springfox.documentation.swagger.web.TagsSorter;

/**
 * @author 翟永超
 * Create date ：2017/8/7.
 * My blog： http://blog.didispace.com
 */
@Data
@ConfigurationProperties("swagger.authorization")
public class SwaggerAuthorizationProperties {

    /**
     * 鉴权策略ID，对应 SecurityReferences ID
     */
    private String name = "Authorization";

    /**
     * 鉴权策略，可选 ApiKey | BasicAuth | None，默认ApiKey
     */
    private String type = "ApiKey";

    /**
     * 鉴权传递的Header参数
     */
    private String keyName = "TOKEN";

    /**
     * 需要开启鉴权URL的正则
     */
    private String authRegex = "^.*$";

}


