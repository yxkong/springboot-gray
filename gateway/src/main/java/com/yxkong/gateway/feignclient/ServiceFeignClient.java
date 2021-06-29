package com.yxkong.gateway.feignclient;

import com.yxkong.common.dto.LoginInfo;
import com.yxkong.common.dto.ResultBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author: yxkong
 * @Date: 2021/6/28 7:22 下午
 * @version: 1.0
 */
@FeignClient("demo-service")
public interface ServiceFeignClient {

    @RequestMapping(value = "/account/getLoginInfo",method = {RequestMethod.POST})
    ResultBean<LoginInfo> getLoginInfo(@RequestHeader(value = "token") String token);
}