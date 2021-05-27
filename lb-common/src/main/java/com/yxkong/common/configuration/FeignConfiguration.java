package com.yxkong.common.configuration;

import com.yxkong.lb.holder.GrayHolder;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

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
                RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
                if(!Objects.isNull(requestAttributes)){
                    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                    template.header(GrayHolder.LABEL_KEY, request.getHeader(GrayHolder.LABEL_KEY));
                    template.header(GrayHolder.VERSION_KEY, request.getHeader(GrayHolder.VERSION_KEY));
                }

            }
        };
    }

}