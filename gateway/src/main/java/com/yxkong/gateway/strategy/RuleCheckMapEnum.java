package com.yxkong.gateway.strategy;

import com.yxkong.gateway.strategy.impl.RuleCheckEnum;

/**
 * @Author: yxkong
 * @Date: 2021/5/13 5:04 下午
 * @version: 1.0
 */
public enum RuleCheckMapEnum {
    GTE("gte",RuleCheckEnum.gte),

    ;
    private String opt;
    private RuleCheckEnum ruleCheckEnum;
    RuleCheckMapEnum(String opt,RuleCheckEnum ruleCheckEnum){
        this.opt = opt;
        this.ruleCheckEnum = ruleCheckEnum;
    }

}