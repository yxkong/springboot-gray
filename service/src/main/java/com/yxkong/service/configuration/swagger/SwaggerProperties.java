package com.yxkong.service.configuration.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.service.ResponseMessage;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * swagger属性配置
 *
 * @Author: yxkong
 * @Date: 2021/6/7 4:06 下午
 * @version: 1.0
 */
@Component
@ConfigurationProperties("swagger")
@Data
public class SwaggerProperties {
    private boolean show;
    /**
     * swagger标题
     */
    private String title;
    /**
     * 描述
     */
    private String description;
    /**
     * 接口版本
     */
    private String version;
    /**
     * 联系姓名
     */
    private String contactName;
    /**
     * 联系url
     */
    private String contactUrl;
    /**
     * 联系邮箱
     */
    private String contactEmail;
    /**
     * 支持url
     */
    private String serviceUrl;
    /**
     * 扫描包
     */
    private String scanPackage;

    /**
     * 添加Request head参数
     */
    private Map<String, RequestParameter> globleRequestParams = new LinkedHashMap<>();
    /**
     * 添加Response head参数
     */
    private Map<String, ResponseMessage> globleResponseMessage = new LinkedHashMap<>();

}