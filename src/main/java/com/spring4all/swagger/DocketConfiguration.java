package com.spring4all.swagger;

import static com.google.common.collect.Lists.newArrayList;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Predicates;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author 程序猿DD
 * @author andi.lin
 *
 *         Created on 2017/8/7 Update on 2021/8/13
 */
@Configuration
@EnableConfigurationProperties(SwaggerProperties.class)
public class DocketConfiguration implements BeanFactoryAware {

    private BeanFactory beanFactory;

    @Autowired
    private SwaggerProperties swaggerProperties;

    @Autowired
    private SwaggerAuthorizationConfiguration authConfiguration;

    private static final String BEAN_NAME = "spring-boot-starter-swagger-";

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /**
     * Create the corresponding configuration for DocumentationPluginRegistry
     */
    @Bean
    public void createSpringFoxRestApi() {
        BeanDefinitionRegistry beanRegistry = (BeanDefinitionRegistry)beanFactory;

        // 没有分组
        if (swaggerProperties.getDocket().size() == 0) {
            String beanName = BEAN_NAME + "default";
            BeanDefinition beanDefinition4Group = new GenericBeanDefinition();
            beanDefinition4Group.getConstructorArgumentValues().addIndexedArgumentValue(0, DocumentationType.OAS_30);
            beanDefinition4Group.setBeanClassName(Docket.class.getName());
            beanDefinition4Group.setRole(BeanDefinition.ROLE_SUPPORT);
            beanRegistry.registerBeanDefinition(beanName, beanDefinition4Group);

            Docket docket4Group = (Docket)beanFactory.getBean(beanName);
            ApiInfo apiInfo = apiInfo(swaggerProperties);
            docket4Group.host(swaggerProperties.getHost()).apiInfo(apiInfo)
                .globalRequestParameters(
                    assemblyRequestParameters(swaggerProperties.getGlobalOperationParameters(), new ArrayList<>()))
                .securityContexts(Collections.singletonList(authConfiguration.securityContext()))
                .securitySchemes(authConfiguration.getSecuritySchemes()).select()
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))
                .paths(paths(swaggerProperties.getBasePath(), swaggerProperties.getExcludePath())).build();
            return;
        }

        for (Map.Entry<String, SwaggerProperties.DocketInfo> entry : swaggerProperties.getDocket().entrySet()) {
            String groupName = entry.getKey();
            SwaggerProperties.DocketInfo docketInfo = entry.getValue();
            String beanName = BEAN_NAME + groupName;

            ApiInfo apiInfo = new ApiInfoBuilder()
                .title(docketInfo.getTitle().isEmpty() ? swaggerProperties.getTitle() : docketInfo.getTitle())
                .description(docketInfo.getDescription().isEmpty() ? swaggerProperties.getDescription()
                    : docketInfo.getDescription())
                .version(docketInfo.getVersion().isEmpty() ? swaggerProperties.getVersion() : docketInfo.getVersion())
                .license(docketInfo.getLicense().isEmpty() ? swaggerProperties.getLicense() : docketInfo.getLicense())
                .licenseUrl(docketInfo.getLicenseUrl().isEmpty() ? swaggerProperties.getLicenseUrl()
                    : docketInfo.getLicenseUrl())
                .contact(new Contact(
                    docketInfo.getContact().getName().isEmpty() ? swaggerProperties.getContact().getName()
                        : docketInfo.getContact().getName(),
                    docketInfo.getContact().getUrl().isEmpty() ? swaggerProperties.getContact().getUrl()
                        : docketInfo.getContact().getUrl(),
                    docketInfo.getContact().getEmail().isEmpty() ? swaggerProperties.getContact().getEmail()
                        : docketInfo.getContact().getEmail()))
                .termsOfServiceUrl(docketInfo.getTermsOfServiceUrl().isEmpty()
                    ? swaggerProperties.getTermsOfServiceUrl() : docketInfo.getTermsOfServiceUrl())
                .build();

            // base-path处理
            // 当没有配置任何path的时候，解析/**
            if (docketInfo.getBasePath().isEmpty()) {
                docketInfo.getBasePath().add("/**");
            }

            List<Predicate<String>> basePath = new ArrayList<>();
            for (String path : docketInfo.getBasePath()) {
                basePath.add(PathSelectors.ant(path));
            }

            // exclude-path处理
            List<Predicate<String>> excludePath = new ArrayList<>();
            for (String path : docketInfo.getExcludePath()) {
                excludePath.add(PathSelectors.ant(path));
            }

            BeanDefinition beanDefinition4Group = new GenericBeanDefinition();
            beanDefinition4Group.getConstructorArgumentValues().addIndexedArgumentValue(0, DocumentationType.OAS_30);
            beanDefinition4Group.setBeanClassName(Docket.class.getName());
            beanDefinition4Group.setRole(BeanDefinition.ROLE_SUPPORT);
            beanRegistry.registerBeanDefinition(beanName, beanDefinition4Group);

            Docket docket4Group = (Docket)beanFactory.getBean(beanName);
            docket4Group.groupName(groupName).host(docketInfo.getBasePackage()).apiInfo(apiInfo)
                .globalRequestParameters(assemblyRequestParameters(swaggerProperties.getGlobalOperationParameters(),
                    docketInfo.getGlobalOperationParameters()))
                .securityContexts(Collections.singletonList(authConfiguration.securityContext()))
                .securitySchemes(authConfiguration.getSecuritySchemes()).select()
                .apis(RequestHandlerSelectors.basePackage(docketInfo.getBasePackage()))
                .paths(paths(docketInfo.getBasePath(), docketInfo.getExcludePath())).build();
        }
    }

    /**
     * 全局请求参数
     *
     * @param properties
     *            {@link SwaggerProperties}
     * @return RequestParameter {@link RequestParameter}
     */
    private List<RequestParameter> getRequestParameters(List<SwaggerProperties.GlobalOperationParameter> properties) {
        if (CollectionUtils.isEmpty(properties)) {
            return new ArrayList<>();
        }

        return properties.stream()
            .map(param -> new RequestParameterBuilder().name(param.getName()).description(param.getDescription())
                .in(ParameterType.from(param.getParameterType())).required(param.getRequired())
                .query(q -> q.defaultValue(param.getType()))
                .query(q -> q.model(m -> m.scalarModel(!ScalarType.from(param.getType(), param.getFormat()).isPresent()
                    ? ScalarType.STRING : ScalarType.from(param.getType(), param.getFormat()).get())))
                .build())
            .collect(Collectors.toList());
    }

    /**
     * 局部参数按照name覆盖局部参数
     *
     * @param globalRequestParameters
     *            全局配置
     * @param groupRequestParameters
     *            Group 的配置
     * @return 汇总配置
     */
    private List<RequestParameter> assemblyRequestParameters(
        List<SwaggerProperties.GlobalOperationParameter> globalRequestParameters,
        List<SwaggerProperties.GlobalOperationParameter> groupRequestParameters) {

        if (Objects.isNull(groupRequestParameters) || groupRequestParameters.isEmpty()) {
            return getRequestParameters(globalRequestParameters);
        }

        Set<String> paramNames = groupRequestParameters.stream()
            .map(SwaggerProperties.GlobalOperationParameter::getName).collect(Collectors.toSet());

        List<SwaggerProperties.GlobalOperationParameter> requestParameters = newArrayList();

        if (Objects.nonNull(globalRequestParameters)) {
            for (SwaggerProperties.GlobalOperationParameter parameter : globalRequestParameters) {
                if (!paramNames.contains(parameter.getName())) {
                    requestParameters.add(parameter);
                }
            }
        }

        requestParameters.addAll(groupRequestParameters);
        return getRequestParameters(requestParameters);
    }

    /**
     * API接口路径选择
     *
     * @param basePath
     *            basePath
     * @param excludePath
     *            excludePath
     * @return path
     */

    private Predicate paths(List<String> basePath, List<String> excludePath) {
        // base-path处理
        // 当没有配置任何path的时候，解析/**
        if (basePath.isEmpty()) {
            basePath.add("/**");
        }
        List<com.google.common.base.Predicate<String>> basePathList = new ArrayList<>();
        for (String path : basePath) {
            basePathList.add(PathSelectors.ant(path));
        }

        // exclude-path处理
        List<com.google.common.base.Predicate<String>> excludePathList = new ArrayList<>();
        for (String path : excludePath) {
            excludePathList.add(PathSelectors.ant(path));
        }

        return Predicates.and(Predicates.not(Predicates.or(excludePathList)), Predicates.or(basePathList));
    }

    /**
     * API文档基本信息
     *
     * @param swaggerProperties
     *            swaggerProperties
     * @return apiInfo
     */
    private ApiInfo apiInfo(SwaggerProperties swaggerProperties) {
        return new ApiInfoBuilder().title(swaggerProperties.getTitle()).description(swaggerProperties.getDescription())
            .version(swaggerProperties.getVersion()).license(swaggerProperties.getLicense())
            .licenseUrl(swaggerProperties.getLicenseUrl())
            .contact(new Contact(swaggerProperties.getContact().getName(), swaggerProperties.getContact().getUrl(),
                swaggerProperties.getContact().getEmail()))
            .termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl()).build();
    }
}
