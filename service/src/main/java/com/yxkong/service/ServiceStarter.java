package com.yxkong.service;

import com.yxkong.lb.configuration.EurekaMetadata;
import com.yxkong.lb.configuration.LoadBalanceAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

/**
 * @Author: yxkong
 * @Date: 2021/5/11 1:44 下午
 * @version: 1.0
 */
@SpringBootApplication(scanBasePackages={"com.yxkong"})
@EnableDiscoveryClient
@EnableEurekaClient
@EnableFeignClients
@Import(EurekaMetadata.class)
@RibbonClients(defaultConfiguration = LoadBalanceAutoConfiguration.class)
public class ServiceStarter {
    public static void main(String[] args) {
        SpringApplication.run(ServiceStarter.class,args);
    }
}