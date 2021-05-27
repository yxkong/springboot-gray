package com.yxkong.common.holder;

import java.util.Map;

/**
 * 异步消费时，从InheritableThreadLocal 获取请求头对应的值
 * @Author: yxkong
 * @Date: 2021/5/27 11:00 上午
 * @version: 1.0
 */
public class ThreadLocalRequestHeader {
    public static final InheritableThreadLocal<Map> CONTEXT_HOLDER = new InheritableThreadLocal<>();
}