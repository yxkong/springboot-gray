package com.yxkong.gateway.strategy.impl;

import com.yxkong.gateway.dto.LoadBalancerRule;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 等于策略处理
 * @Author: yxkong
 * @Date: 2021/5/13 5:06 下午
 * @version: 1.0
 */
@Service("eqRuleStrategy")
public class EqRuleStrategy extends BaseRuleStrategyImpl {

    @Override
    public boolean isRoute(LoadBalancerRule rule, Map<String, String> loginInfo) {
        if(loginInfo.get(rule.getFiled()).equalsIgnoreCase(rule.getVal())){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}