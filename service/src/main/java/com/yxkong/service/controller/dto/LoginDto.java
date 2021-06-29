package com.yxkong.service.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: yxkong
 * @Date: 2021/6/27 5:14 下午
 * @version: 1.0
 */
@Data
@Builder
@Schema(name="登录反参")
@NoArgsConstructor
public class LoginDto {
    public LoginDto(String token) {
        this.token = token;
    }

    private String token;
}