package com.spring4all.swagger;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import springfox.documentation.swagger.web.DocExpansion;
import springfox.documentation.swagger.web.ModelRendering;
import springfox.documentation.swagger.web.OperationsSorter;
import springfox.documentation.swagger.web.TagsSorter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 翟永超
 * Create date ：2017/8/7.
 * My blog： http://blog.didispace.com
 */
@Data
@ConfigurationProperties("swagger.ui-config")
public class SwaggerUiProperties {

    private String apiSorter = "alpha";

    /**
     * 是否启用json编辑器
     **/
    private Boolean jsonEditor = false;
    /**
     * 是否显示请求头信息
     **/
    private Boolean showRequestHeaders = true;
    /**
     * 支持页面提交的请求类型
     **/
    private String submitMethods = "get,post,put,delete,patch";
    /**
     * 请求超时时间
     **/
    private Long requestTimeout = 10000L;

    private Boolean deepLinking;
    private Boolean displayOperationId;
    private Integer defaultModelsExpandDepth;
    private Integer defaultModelExpandDepth;
    private ModelRendering defaultModelRendering;

    /**
     * 是否显示请求耗时，默认false
     */
    private Boolean displayRequestDuration = true;
    /**
     * 可选 none | list
     */
    private DocExpansion docExpansion;
    /**
     * Boolean=false OR String
     */
    private Object filter;
    private Integer maxDisplayedTags;
    private OperationsSorter operationsSorter;
    private Boolean showExtensions;
    private TagsSorter tagsSorter;

    /**
     * Network
     */
    private String validatorUrl;

}


