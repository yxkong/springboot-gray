/**
 * @Author: yxkong
 * @Date: 2021/6/26 6:13 δΈε
 * @version: 1.0
 */
package com.yxkong.common.plugin.context;

import javax.servlet.http.HttpServletRequest;

/**
 * γγ
 *
 * @author ducongcong
 * @create 2021/6/26
 * @since 1.0.0
 */
public interface SecurityContext<T> {
    T getContext();
}
