package com.yxkong.lb.configuration;

import com.yxkong.common.plugin.context.SecurityContextHolder;
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
                    /**
                     * 使用InheritableThreadLocal 保证在开启多线程时，子线程的feign调用能获取到缓存的header值
                     */
                    SecurityContextHolder.clearContext();
                    SecurityContextHolder.setRequest(request);
                    /**
                     * initHystrixRequestContext 只能保证Hystrix内部，如果异步或者开启多线程
                     * 子线程是拿不到结果的，如果是线程池调用，在任务里重新进行initHystrixRequestContext
                     */

                    GrayHolder.initHystrixRequestContext(request.getHeader(GrayHolder.LABEL_KEY),request.getHeader(GrayHolder.VERSION_KEY));
                    return true;
                }

                @Override
                public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
                    SecurityContextHolder.clearContext();
                    GrayHolder.shutdownHystrixRequestContext();
                }
            };
        }
    }
}