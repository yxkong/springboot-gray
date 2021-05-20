package com.yxkong.lb.holder;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableDefault;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 灰度
 *
 * @Author: yxkong
 * @Date: 2021/5/13 1:45 下午
 * @version: 1.0
 */
public class GrayHolder {
    public static final String LABEL_KEY = "label";
    public static final String LABEL_VAL = "gray";
    public static final String VERSION_KEY = "version";
    public static final HystrixRequestVariableDefault<Map<String, String>> label = new HystrixRequestVariableDefault<>();

    /**
     * 初始化HystrixRequestContext，并设置lable标签和version版本号
     * @param lable 设置标签
     * @param version 设置版本号
     */
    public static void initHystrixRequestContext(String lable,String version){
        //内部使用threadlocal
        HystrixRequestContext.initializeContext();
        Map<String, String> map = new HashMap<>();
        if(!StringUtils.isEmpty(lable)){
            map.put(LABEL_KEY,lable);
        }
        if(!StringUtils.isEmpty(version)){
            map.put(VERSION_KEY,version);
        }
        GrayHolder.label.set(map);
    }

    /**
     *从HystrixRequestVariableDefault 的threadlocal中获取lable
     * @return
     */
    public static String getLable(){
        Map<String, String> map = GrayHolder.label.get();
        if(null == map){
            return null;
        }
        return map.getOrDefault(LABEL_KEY,null);
    }
    public static String getVersion(){
        Map<String, String> map = GrayHolder.label.get();
        if(null == map){
            return null;
        }
        return map.getOrDefault(VERSION_KEY,null);
    }

    /**
     * 在http请求执行完之后进行清除，
     * 可以是spring的实现机制，WebMvcConfigurer 中 HandlerInterceptor.afterCompletion
     * 也可以是servlet的实现
     */
    public static void shutdownHystrixRequestContext() {
        if (initialized()) {
            HystrixRequestContext.getContextForCurrentThread().shutdown();
        }
    }
    public static boolean initialized() {
        return HystrixRequestContext.isCurrentThreadInitialized();
    }
}