package com.yxkong.lb.configuration;

import com.netflix.loadbalancer.IRule;
import com.yxkong.common.constant.Constants;
import com.yxkong.lb.LabelRule;
import com.yxkong.lb.interceptor.GrayRequestInterceptor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.cloud.netflix.ribbon.eureka.EurekaRibbonClientConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: yxkong
 * @Date: 2021/5/17 3:45 下午
 * @version: 1.0
 */
@Configuration
@ConditionalOnClass({ DiscoveryClient.class, EurekaInstanceConfigBean.class })
@AutoConfigureAfter(EurekaRibbonClientConfiguration.class)
public class LoadBalanceAutoConfiguration {
    @Bean
    public IRule ribbonRule() {
        return  new LabelRule();
    }
    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        // 单位为ms
        factory.setReadTimeout(Constants.DEFAULT_READ_TIMEOUT);
        // 单位为ms
        factory.setConnectTimeout(Constants.DEFAULT_CONNECT_TIMEOUT);
        return factory;
    }
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.getInterceptors().add(new GrayRequestInterceptor());
        return restTemplate;
    }
}