package com.yxkong.gateway.strategy.impl;

import com.yxkong.gateway.dto.LoadBalancerRule;
import com.yxkong.gateway.strategy.RuleStrategy;

import java.util.Map;

/**
 * @Author: yxkong
 * @Date: 2021/5/13 4:10 下午
 * @version: 1.0
 */
public enum RuleCheckEnum implements RuleStrategy {
    /**
     * 大于等于,字段类型必须是Long,年龄也用
     */
    gte{
        @Override
        public boolean preValid(LoadBalancerRule rule, Map<String, String> loginInfo) {
            return false;
        }

        @Override
        public boolean isRoute(LoadBalancerRule rule,  Map<String,String> loginInfo) {
            if(null == loginInfo.get(rule.getFiled())){
                return Boolean.FALSE;
            }
            if(Long.parseLong(loginInfo.get(rule.getFiled())) >= Long.parseLong(rule.getVal())){
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }
    },
    /**
     * 大于
     */
    ge{
        @Override
        public boolean preValid(LoadBalancerRule rule, Map<String, String> loginInfo) {
            return false;
        }
        @Override
        public boolean isRoute(LoadBalancerRule rule, Map<String,String> loginInfo) {
            return false;
        }
    },
    /**
     * 小于等于
     */
    lte{
        @Override
        public boolean preValid(LoadBalancerRule rule, Map<String, String> loginInfo) {
            return false;
        }
        @Override
        public boolean isRoute(LoadBalancerRule rule, Map<String, String> loginInfo) {
            return false;
        }
    },
    /**
     * 小于
     */
    le{
        @Override
        public boolean preValid(LoadBalancerRule rule, Map<String, String> loginInfo) {
            return false;
        }
        @Override
        public boolean isRoute(LoadBalancerRule rule, Map<String, String> loginInfo) {
            return false;
        }
    },
    /**
     * 等于
     */
    eq{
        @Override
        public boolean preValid(LoadBalancerRule rule, Map<String, String> loginInfo) {
            return false;
        }
        @Override
        public boolean isRoute(LoadBalancerRule rule, Map<String, String> loginInfo) {
            return false;
        }
    },
    /**
     * 在什么里,配置时，以#分隔
     */
    in{
        @Override
        public boolean preValid(LoadBalancerRule rule, Map<String, String> loginInfo) {
            return false;
        }
        @Override
        public boolean isRoute(LoadBalancerRule rule, Map<String, String> loginInfo) {
            if(null == loginInfo.get(rule.getFiled())){
                return Boolean.FALSE;
            }
            if(rule.getVal().contains(loginInfo.get(rule.getFiled())+"#")){
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }
    },
    /**
     * 取模,
     */
    mod10{
        @Override
        public boolean preValid(LoadBalancerRule rule, Map<String, String> loginInfo) {
            return false;
        }
        @Override
        public boolean isRoute(LoadBalancerRule rule, Map<String, String> loginInfo) {
            if(null == loginInfo.get(rule.getFiled())){
                return Boolean.FALSE;
            }
            long mod = Long.parseLong(loginInfo.get(rule.getFiled()))%10L;
            if(rule.getVal().contains(mod+"")){
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }
    }

}
