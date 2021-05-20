package com.yxkong.common.configuration;

import com.yxkong.common.filter.HttpTraceLogFilter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: yxkong
 * @Date: 2021/5/19 9:29 上午
 * @version: 1.0
 */
@Configuration
@ConditionalOnWebApplication
public class HttpTraceConfiguration {
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    static class ServletTraceFilterConfiguration {

        @Bean
        @ConditionalOnProperty(name = "yxkong.http.trace.enabled", havingValue = "true")
        public HttpTraceLogFilter httpTraceLogFilter(MeterRegistry registry) {
            return new HttpTraceLogFilter(registry);
        }
    }
}