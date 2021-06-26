package com.yxkong.common.plugin.context;

/**
 * @Author: yxkong
 * @Date: 2021/6/26 6:16 下午
 * @version: 1.0
 */
public class ThreadLocalSecurityContextHolderStrategy implements SecurityContextHolderStrategy{
    private static final InheritableThreadLocal<SecurityContext> contextHolder = new InheritableThreadLocal<SecurityContext>();

    @Override
    public void clearContext() {
        contextHolder.remove();
    }

    @Override
    public SecurityContext getContext() {
        //默认是null，
        SecurityContext ctx = contextHolder.get();

        if (ctx == null) {
            ctx = createEmptyContext();
            contextHolder.set(ctx);
        }

        return ctx;
    }

    @Override
    public void setContext(SecurityContext context) {
        contextHolder.set(context);
    }

    @Override
    public SecurityContext createEmptyContext() {
        return new SecurityContextImpl();
    }
}