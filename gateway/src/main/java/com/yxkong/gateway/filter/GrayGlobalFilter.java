package com.yxkong.gateway.filter;

import com.googlecode.aviator.AviatorEvaluator;
import com.yxkong.common.constant.HeaderConstant;
import com.yxkong.common.dto.LoginInfo;
import com.yxkong.common.enums.OrderEnum;
import com.yxkong.common.utils.JsonUtils;
import com.yxkong.gateway.entity.gateway.GrayRule;
import com.yxkong.gateway.mapper.gateway.GrayRuleMapper;
import com.yxkong.gateway.strategy.RuleStrategy;
import com.yxkong.lb.holder.GrayHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 灰度拦截处理
 * @Author: yxkong
 * @Date: 2021/5/12 9:32 上午
 * @version: 1.0
 */
@Component
public class GrayGlobalFilter implements GlobalFilter, Ordered {
    private static Logger log = LoggerFactory.getLogger(GrayGlobalFilter.class);
    @Autowired
    private Map<String, RuleStrategy> ruleStrategyMap;
    @Resource
    private GrayRuleMapper grayRuleMapper;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String info = exchange.getRequest().getHeaders().getFirst(HeaderConstant.LOGIN_INFO);
        ServerHttpRequest request = null;
        //配置规则与对应的版本
        //配置规则与对应的版本
        String version = null;
        String label = null;
        if (Objects.nonNull(info)){
            try {
                info = URLDecoder.decode(info, StandardCharsets.UTF_8.name());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            final LoginInfo loginInfo = JsonUtils.fromJson(info, LoginInfo.class);
            if (Objects.nonNull(loginInfo)){
                final Map<String, Object> map = initMap(loginInfo);
                final GrayRule grayRule = grayRuleMapper.findOne();
                Boolean flag = false;
                if (Objects.nonNull(grayRule)){
                    flag = (Boolean) AviatorEvaluator.execute(grayRule.getRule(), map);
                    log.info("用户{} {} 灰度用户",loginInfo.getUserId(),flag);
                    if(flag){
                        version = grayRule.getVersion();
                        label = grayRule.getLable();
                    }
                }

            }
            request = exchange.getRequest().mutate()
                    .header(GrayHolder.VERSION_KEY, version)
                    .header(GrayHolder.LABEL_KEY,label)
                    .build();

        } else {
            request = exchange.getRequest().mutate().build();
        }
        GrayHolder.initHystrixRequestContext(label,version);
        return chain.filter(exchange.mutate().request(request).build());
    }

    /**
     * 初始化规则包的元数据，配置时从这里取值
     * @param loginInfo
     * @return
     */
    private Map<String,Object> initMap(LoginInfo loginInfo){
        Map<String,Object> map = new HashMap<>();
        map.put("userId",loginInfo.getUserId());
        map.put("age",loginInfo.getAge());
        map.put("loginSource",loginInfo.getLoginSource());
        map.put("mobile",loginInfo.getMobile());
        map.put("registerTime",loginInfo.getRegisterTime());
        if (Objects.nonNull(loginInfo.getMobile())){
            long mod = Long.parseLong(loginInfo.getMobile())%10L;
            map.put("mobileMod",String.valueOf(mod));
        }
        map.put("userIdMod",String.valueOf(loginInfo.getUserId()%10L));
        return map;
    }

    @Override
    public int getOrder() {
        return OrderEnum.GRAY.getOrder();
    }
}