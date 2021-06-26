package com.yxkong.common.configuration;

import com.yxkong.lb.holder.GrayHolder;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * feign拦截器
 *
 * @Author: yxkong
 * @Date: 2021/5/27 10:55 上午
 * @version: 1.0
 */
@Configuration
public class FeignConfiguration {
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor applyRequestInterceptor(){
        //将特定的参数通过Feign往下传递
        return new RequestInterceptor(){
            @Override
            public void apply(RequestTemplate template) {
                template.header(GrayHolder.LABEL_KEY, GrayHolder.getLable());
                template.header(GrayHolder.VERSION_KEY, GrayHolder.getVersion());
            }
        };
    }

}