package com.yxkong.gateway.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @Author: yxkong
 * @Date: 2021/5/12 10:47 上午
 * @version: 1.0
 */
@Configuration
@ConfigurationProperties(prefix = "gateway.auth.exclude")
public class AuthProperties {
    private List<String> hosts;
    private List<String> urls;

    public List<String> getHosts() {
        return hosts;
    }

    public void setHosts(List<String> hosts) {
        this.hosts = hosts;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
}