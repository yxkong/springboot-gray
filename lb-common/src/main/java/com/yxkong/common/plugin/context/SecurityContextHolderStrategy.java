/**
 * @Author: yxkong
 * @Date: 2021/6/26 6:15 下午
 * @version: 1.0
 */
package com.yxkong.common.plugin.context;

/**
 * 〈〉
 *
 * @author ducongcong
 * @create 2021/6/26
 * @since 1.0.0
 */
public interface SecurityContextHolderStrategy {
    /**
     * Clears the current context.
     */
    void clearContext();

    /**
     * Obtains the current context.
     *
     * @return a context (never <code>null</code> - create a default implementation if necessary)
     */
    SecurityContext getContext();

    /**
     * Sets the current context.
     *
     * @param context to the new argument (should never be <code>null</code>, although implementations must check if
     *        <code>null</code> has been passed and throw an <code>IllegalArgumentException</code> in such cases)
     */
    void setContext(SecurityContext context);

    /**
     * Creates a new, empty context implementation, for use by <tt>SecurityContextRepository</tt> implementations,
     * when creating a new context for the first time.
     *
     * @return the empty context.
     */
    SecurityContext createEmptyContext();
}
