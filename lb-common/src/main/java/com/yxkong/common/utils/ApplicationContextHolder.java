package com.yxkong.common.utils;

import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * @Author: yxkong
 * @Date: 2021/5/17 3:40 下午
 * @version: 1.0
 */
public class ApplicationContextHolder {
    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        ApplicationContextHolder.applicationContext = applicationContext;
    }

    /**
     * 根据beanId获取容器中的bean
     *
     * @param name
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        return (T) applicationContext.getBean(name);
    }

    /**
     * 根据类型获取所有的bean
     *
     * @param type
     * @param <T>
     * @return
     */
    public static <T> Map<String, T> getBeansOfType(Class<T> type) {
        return applicationContext.getBeansOfType(type);
    }
    public static <T> T getBean(Class<T> clazz) {

        if (clazz == null) {
            return null;
        }
        return (T) applicationContext.getBean(clazz);
    }
}