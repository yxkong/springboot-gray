/**
  * 规则策略
  * @Author:   yxkong
  * @Date:     2021/5/13 4:07 下午
  * @version:  1.0
  */
package com.yxkong.gateway.strategy;

import com.yxkong.gateway.dto.LoadBalancerRule;
import com.yxkong.gateway.dto.LoginInfo;
import org.springframework.core.Ordered;

import java.util.Map;

/**
 * 〈规则策略〉
 *
 * @author ducongcong
 * @create 2021/5/13
 * @since 1.0.0
 */
public interface RuleStrategy  {
    /**
     * 前置公共校验
     * @param rule
     * @param loginInfo
     * @return
     */
    boolean preValid(LoadBalancerRule rule, Map<String,String> loginInfo);
    /**
     * 是否路由
     * @param rule
     * @return
     */
    boolean isRoute(LoadBalancerRule rule, Map<String,String> loginInfo);
}
