package com.didispace.swagger;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 翟永超
 * @create 2017/8/7.
 * @blog http://blog.didispace.com
 */
@Data
@ConfigurationProperties("swagger")
public class SwaggerProperties {

    /**标题**/
    private String title = "";
    /**描述**/
    private String description = "";
    /**版本**/
    private String version = "";
    /**许可证**/
    private String license = "";
    /**许可证URL**/
    private String licenseUrl = "";
    /**服务条款URL**/
    private String termsOfServiceUrl = "";

    private Contact contact = new Contact();

    /**swagger会解析的url规则**/
    private List<String> basePath = new ArrayList<>();
    /**在basePath基础上需要排除的url规则**/
    private List<String> excludePath = new ArrayList<>();


    @Data
    @NoArgsConstructor
    public static class Contact {

        /**联系人**/
        private String name = "";
        /**联系人url**/
        private String url = "";
        /**联系人email**/
        private String email = "";

    }

}


