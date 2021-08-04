package com.yxkong.common.configuration.sleuth;

import com.yxkong.common.configuration.sleuth.filter.TracingFilter;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: yxkong
 * @Date: 2021/7/27 3:15 下午
 * @version: 1.0
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "yxkong.tracing")
@ConditionalOnProperty(name = "yxkong.tracing.enabled", havingValue = "true", matchIfMissing = true)
public class SleuthFilterConfigurer {
    private boolean enabled;

    @Bean
    public FilterRegistrationBean<TracingFilter> tracingFilterRegistrationBean() {
        FilterRegistrationBean<TracingFilter> filterRegistrationBean = new FilterRegistrationBean<TracingFilter>();
        filterRegistrationBean.setFilter(new TracingFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("TracingInterceptor");
        return filterRegistrationBean;
    }
}