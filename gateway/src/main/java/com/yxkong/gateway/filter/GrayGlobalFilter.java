package com.yxkong.gateway.filter;

import com.alibaba.fastjson.TypeReference;
import com.google.gson.Gson;
import com.yxkong.common.constant.HeaderConstant;
import com.yxkong.common.enums.OrderEnum;
import com.yxkong.common.utils.FastJsonUtils;
import com.yxkong.gateway.dto.LoadBalancerRule;
import com.yxkong.gateway.strategy.RuleStrategy;
import com.yxkong.lb.holder.GrayHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 灰度拦截处理
 * @Author: yxkong
 * @Date: 2021/5/12 9:32 上午
 * @version: 1.0
 */
@Component
public class GrayGlobalFilter implements GlobalFilter, Ordered {
    @Autowired
    private Map<String, RuleStrategy> ruleStrategyMap;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String info = exchange.getRequest().getHeaders().getFirst(HeaderConstant.LOGIN_INFO);
        try {
            info = URLDecoder.decode(info, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Map<String,String> loginMap = FastJsonUtils.fromJson(info,new TypeReference<Map<String, String>>(){});
        if(loginMap!= null && !loginMap.isEmpty()){
            List<LoadBalancerRule> rules = getRules();
            boolean flag = Boolean.FALSE;
            /**
             * 这除规则可以利用表结构来配置化，这个规则，可能是多个字段的组合，也可能只是取模，
             * 固化一些规则，如：
             * 注册时间是指定时间的
             * 登录时间是指定时间的
             * userId取模,对应几个
             * 手机号尾号是
             * 手机号取模
             */
            //只实现了并且的关系，只要有一个为false就不走灰度
            for (LoadBalancerRule r:rules){
                flag = ruleStrategyMap.get(getRuleName(r.getOperator())).isRoute(r,loginMap);
            }
            if(flag){
                //配置规则与对应的版本
                String version = "2.0";
                String lable = "gray";
                GrayHolder.initHystrixRequestContext(lable,version);
                ServerHttpRequest request = exchange.getRequest().mutate()
                        .header(GrayHolder.VERSION_KEY, version)
                        .header(GrayHolder.LABEL_KEY,lable)
                        .build();
                return chain.filter(exchange.mutate().request(request).build());
            }
        }
        return chain.filter(exchange.mutate().build());
    }
    private String getRuleName(String opt){
        return opt+"RuleStrategy";
    }
    private List<LoadBalancerRule> getRules(){
        List<LoadBalancerRule> rules = new ArrayList<>();
        rules.add(new LoadBalancerRule("mobile","mod10","6,9","and"));
        rules.add(new LoadBalancerRule("userId","gte","900","and"));
        return rules;
    }


    @Override
    public int getOrder() {
        return OrderEnum.GRAY.getOrder();
    }
}