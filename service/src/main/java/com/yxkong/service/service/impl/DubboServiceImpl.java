package com.yxkong.service.service.impl;

import com.yxkong.common.dto.ResultBean;
import com.yxkong.dubbo.service.IDubboService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Service;

/**
 * @Author: yxkong
 * @Date: 2021/6/29 9:06 下午
 * @version: 1.0
 */
@DubboService
public class DubboServiceImpl implements IDubboService {
    @Override
    public ResultBean findInfo(String key) {
        return null;
    }
}