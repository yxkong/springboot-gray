package com.yxkong.service.configuration.swagger;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.*;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * swagger3集成
 * 访问地址：http://127.0.0.1:8001/swagger-ui/index.html
 * @Author: yxkong
 * @Date: 2021/6/7 3:55 下午
 * @version: 1.0
 */
@EnableOpenApi
@Configuration
public class Swagger3Configuration  {

    private final SwaggerProperties swaggerProperties;

    public Swagger3Configuration(SwaggerProperties swaggerProperties) {
        this.swaggerProperties = swaggerProperties;
    }
    @Bean
    public Docket createRestApi() {
        /**
         * 构建并返回文档摘要信息
         */
        return new Docket(DocumentationType.OAS_30).pathMapping("/")
                // 定义是否开启swagger，false为关闭，可以通过变量控制
                .enable(swaggerProperties.isShow())
                // 将api的元信息设置为包含在json ResourceListing响应中。
                .apiInfo(apiInfo())
                // 接口调试地址
                .host(swaggerProperties.getServiceUrl())
                // 选择哪些接口作为swagger的doc发布
                .select()
                //.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                // .apis(RequestHandlerSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("com.yxkong"))
                .paths(PathSelectors.any())
                .build()
                // 支持的通讯协议集合
                //.protocols(newHashSet("https", "http"))
                //// 授权信息设置，必要的header token等认证信息
                //.securitySchemes(securitySchemes())
                //// 授权信息全局应用
                //.securityContexts(securityContexts());
                .globalRequestParameters(getGlobalRequestParameters())
                .globalResponses(HttpMethod.GET, getGlobalResonseMessage())
                .globalResponses(HttpMethod.POST, getGlobalResonseMessage());
    }

    /**
     * 构建ApiInfo 用于页面上半部分展示
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .contact(new Contact(swaggerProperties.getContactName(), swaggerProperties.getContactUrl(), swaggerProperties.getContactEmail()))
                .version(swaggerProperties.getVersion())
                .build();
    }

    //生成全局通用参数
    private List<RequestParameter> getGlobalRequestParameters() {
        List<RequestParameter> parameters = new ArrayList<>();
        //parameters.add(new RequestParameterBuilder()
        //        .name("proId")
        //        .description("渠道id")
        //        // .required(true)
        //        .in(ParameterType.HEADER)
        //        .query(q -> q.model(m -> m.scalarModel(ScalarType.STRING)))
        //        // .required(false)
        //        .build());
        parameters.add(new RequestParameterBuilder()
                .name("token")
                .description("登录token")
                // .required(true)
                .in(ParameterType.HEADER)
                .query(q -> q.model(m -> m.scalarModel(ScalarType.STRING)))
                .required(false)
                .build());
        return parameters;
    }


    private List<Response> getGlobalResonseMessage() {
        List<Response> responseList = new ArrayList<>();
        responseList.add(new ResponseBuilder().code("404").description("not found").build());
        return responseList;
    }


}