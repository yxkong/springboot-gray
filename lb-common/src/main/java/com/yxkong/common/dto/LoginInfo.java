package com.yxkong.common.dto;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class LoginInfo implements Serializable {
    private Long userId;
    private String mobile;
    private String nickName;
    private Date loginTime;
    private String loginSource;
    private int age;
    private Date registerTime;

}