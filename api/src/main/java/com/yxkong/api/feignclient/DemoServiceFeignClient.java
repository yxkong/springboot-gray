package com.yxkong.api.feignclient;

import com.yxkong.common.dto.ResultBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author: yxkong
 * @Date: 2021/5/19 5:51 下午
 * @version: 1.0
 */
@FeignClient("demo-service")
public interface DemoServiceFeignClient {
    @RequestMapping(value = "/demo/hello",method = {RequestMethod.GET})
    ResultBean hello();

}