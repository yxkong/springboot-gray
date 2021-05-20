package com.yxkong.gateway.strategy.impl;

import com.yxkong.gateway.dto.LoadBalancerRule;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 小于等于策略处理
 * @Author: yxkong
 * @Date: 2021/5/13 5:06 下午
 * @version: 1.0
 */
@Service("mod10RuleStrategy")
public class Mod10RuleStrategy extends BaseRuleStrategyImpl {

    @Override
    public boolean isRoute(LoadBalancerRule rule, Map<String, String> loginInfo) {
        long mod = Long.parseLong(loginInfo.get(rule.getFiled()))%10L;
        if(rule.getVal().contains(mod+"")){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}