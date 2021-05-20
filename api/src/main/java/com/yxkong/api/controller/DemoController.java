package com.yxkong.api.controller;

import com.yxkong.api.feginclient.DemoServiceFeginClient;
import com.yxkong.common.dto.ResultBean;
import com.yxkong.common.holder.RequestHolder;
import com.yxkong.lb.holder.GrayHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    private DemoServiceFeginClient demoServiceFeginClient;

    @GetMapping("hello")
    public ResultBean hello(){
        String msg = "api进入的版本号是："+version;
        log.info(msg);
        return demoServiceFeginClient.hello();
    }
}