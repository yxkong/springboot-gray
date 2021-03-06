package com.yxkong.api;

import com.yxkong.lb.configuration.EurekaMetadata;
import com.yxkong.lb.configuration.LoadBalanceAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author: yxkong
 * @Date: 2021/5/11 1:49 下午
 * @version: 1.0
 */
//外部定义的包，要么定义为starter，要么指定具体的包，要不然扫描不到
@SpringBootApplication(scanBasePackages={"com.yxkong"})
@EnableDiscoveryClient
@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.yxkong.api"})
//@ServletComponentScan(basePackages = {"com.yxkong"})
@Import(EurekaMetadata.class)
@RibbonClients(defaultConfiguration = LoadBalanceAutoConfiguration.class)
@EnableScheduling
public class ApiStarter {

    public static void main(String[] args) {
        SpringApplication.run(ApiStarter.class,args);
    }
}