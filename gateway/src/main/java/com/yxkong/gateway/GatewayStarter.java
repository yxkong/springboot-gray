package com.yxkong.gateway;

import com.yxkong.lb.configuration.EurekaMetadata;
import com.yxkong.lb.configuration.LoadBalanceAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Import;

/**
 * 网关启动器
 *
 * @Author: yxkong
 * @Date: 2021/5/11 12:35 下午
 * @version: 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableEurekaClient
@EnableHystrix
@Import(EurekaMetadata.class)
@RibbonClients(defaultConfiguration = LoadBalanceAutoConfiguration.class)
public class GatewayStarter {
    public static void main(String[] args) {
        SpringApplication.run(GatewayStarter.class,args);
    }

}