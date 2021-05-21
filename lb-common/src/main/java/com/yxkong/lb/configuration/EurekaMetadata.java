package com.yxkong.lb.configuration;

import com.yxkong.common.constant.Constants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * eureka 元数据
 * @Author: yxkong
 * @Date: 2021/5/17 3:30 下午
 * @version: 1.0
 */
@Configuration
@ConfigurationProperties(prefix = Constants.METADATA_MAP)
public class EurekaMetadata {
    private String version;
    private String label;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}