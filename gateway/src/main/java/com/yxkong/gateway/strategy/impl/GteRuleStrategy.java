package com.yxkong.gateway.strategy.impl;

import com.yxkong.gateway.dto.LoadBalancerRule;
import com.yxkong.gateway.strategy.RuleStrategy;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 大于等于策略处理
 * @Author: yxkong
 * @Date: 2021/5/13 5:06 下午
 * @version: 1.0
 */
@Service("gteRuleStrategy")
public class GteRuleStrategy extends BaseRuleStrategyImpl {

    @Override
    public boolean isRoute(LoadBalancerRule rule, Map<String, String> loginInfo) {
        if(Long.parseLong(loginInfo.get(rule.getFiled())) >= Long.parseLong(rule.getVal())){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}