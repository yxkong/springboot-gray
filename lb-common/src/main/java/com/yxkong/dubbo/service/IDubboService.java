/**
 * @Author: yxkong
 * @Date: 2021/6/29 9:05 下午
 * @version: 1.0
 */
package com.yxkong.dubbo.service;

import com.yxkong.common.dto.ResultBean;

/**
 * 〈〉
 *
 * @author ducongcong
 * @create 2021/6/29
 * @since 1.0.0
 */
public interface IDubboService {
    /**
     * 查找信息
     * @param key
     * @return
     */
    ResultBean findInfo(String key);
}
