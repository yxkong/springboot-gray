package com.yxkong.gateway.strategy.impl;

import com.yxkong.gateway.dto.LoadBalancerRule;
import com.yxkong.gateway.strategy.RuleStrategy;

import java.util.Map;

/**
 * 基础校验
 * @Author: yxkong
 * @Date: 2021/5/13 5:08 下午
 * @version: 1.0
 */
public abstract class BaseRuleStrategyImpl implements RuleStrategy {

    @Override
    public boolean preValid(LoadBalancerRule rule, Map<String, String> loginInfo) {
        if(null == loginInfo.get(rule.getFiled())){
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}