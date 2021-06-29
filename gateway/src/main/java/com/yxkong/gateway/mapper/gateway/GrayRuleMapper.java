package com.yxkong.gateway.mapper.gateway;

import com.yxkong.gateway.entity.gateway.GrayRule;

public interface GrayRuleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GrayRule record);

    int insertSelective(GrayRule record);

    GrayRule selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GrayRule record);

    int updateByPrimaryKey(GrayRule record);
    GrayRule findOne();
}