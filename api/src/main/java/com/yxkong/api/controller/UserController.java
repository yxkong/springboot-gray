package com.yxkong.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: yxkong
 * @Date: 2021/5/11 5:23 下午
 * @version: 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("info")
    public String info(){
        return "userinfo";
    }
}