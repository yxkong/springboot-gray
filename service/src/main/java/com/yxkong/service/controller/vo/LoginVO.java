package com.yxkong.service.controller.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Author: yxkong
 * @Date: 2021/6/28 3:40 下午
 * @version: 1.0
 */
@Data
@Schema(name="登录参数")
public class LoginVO {

    private String mobile;
    private String verifyCode;
    private String source;
}