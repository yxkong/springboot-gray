package com.yxkong.common.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: yxkong
 * @Date: 2021/6/28 10:50 上午
 * @version: 1.0
 */
@Data
@Builder
@NoArgsConstructor
public class LoginInfo implements Serializable {
    public LoginInfo(Long userId, String mobile, String nickName, Date loginTime, String loginSource, int age, Date registerTime) {
        this.userId = userId;
        this.mobile = mobile;
        this.nickName = nickName;
        this.loginTime = loginTime;
        this.loginSource = loginSource;
        this.age = age;
        this.registerTime = registerTime;
    }

    private Long userId;
    private String mobile;
    private String nickName;
    private Date loginTime;
    private String loginSource;
    private int age;
    private Date registerTime;

}