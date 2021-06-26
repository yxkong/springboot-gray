package com.yxkong.lb.configuration;

import com.yxkong.lb.holder.GrayHolder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.WebHandler;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: yxkong
 * @Date: 2021/5/19 3:31 下午
 * @version: 1.0
 */
@Configuration
@ConditionalOnClass(Servlet.class)
public class ServletAutoConfiguration {
    @Bean
    @ConditionalOnClass(WebHandler.class)
    public WebMvcConfigurer configurer() {
        return new WebConfig();
    }

    class WebConfig implements WebMvcConfigurer {
        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(interceptor()).addPathPatterns("/**");
        }

        private HandlerInterceptor interceptor() {
            return new HandlerInterceptor() {
                @Override
                public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                    //SecurityContextHolder.clearContext();
                    //SecurityContextHolder.setRequest(request);
                    GrayHolder.initHystrixRequestContext(request.getHeader(GrayHolder.LABEL_KEY),request.getHeader(GrayHolder.VERSION_KEY));
                    return true;
                }

                @Override
                public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
                    //SecurityContextHolder.clearContext();
                    GrayHolder.shutdownHystrixRequestContext();
                }
            };
        }
    }
}