package com.yxkong.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: yxkong
 * @Date: 2021/5/12 9:20 上午
 * @version: 1.0
 */
@RestController
@RequestMapping("/discovery")
public class DiscoveryController {
    @Autowired
    private DiscoveryClient discoveryClient;
    @GetMapping("list")
    public Object list(){
        return discoveryClient.getServices();
    }
    @GetMapping("info")
    public Object info(String serviceId){
        return discoveryClient.getInstances(serviceId);
    }
}