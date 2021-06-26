package com.yxkong.common.plugin.context;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @Author: yxkong
 * @Date: 2021/6/26 6:13 下午
 * @version: 1.0
 */
public class SecurityContextImpl<T> implements SecurityContext{
    private T context;

    public SecurityContextImpl(T context) {
        this.context = context;
    }

    public SecurityContextImpl() {
    }

    @Override
    public T getContext() {
        return this.context;
    }
    public void setContext(T context) {
        this.context = context;
    }

}