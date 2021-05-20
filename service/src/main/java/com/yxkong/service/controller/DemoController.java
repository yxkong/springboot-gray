package com.yxkong.service.controller;

import com.yxkong.common.dto.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: yxkong
 * @Date: 2021/5/11 2:49 下午
 * @version: 1.0
 */
@RestController
@RequestMapping("/demo")
public class DemoController {
    private Logger log = LoggerFactory.getLogger(DemoController.class);
    @Value("${eureka.instance.metadata-map.version}")
    private String version;


    @GetMapping("hello")
    public ResultBean hello(){
        String msg = "service进入的版本号是："+version;
        log.info(msg);
        return new ResultBean.Builder().success(msg).build();
    }


}