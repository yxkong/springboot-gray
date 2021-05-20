package com.yxkong.common.enums;

/**
 * 统一spring的Ordered
 * @Author: yxkong
 * @Date: 2021/5/12 9:47 上午
 * @version: 1.0
 */
public enum OrderEnum {
    AUTH(-100,"鉴权"),
    GRAY(-99,"灰度");
    private int order;
    private String desc;


    OrderEnum(int order,String desc){
        this.order = order;
        this.desc = desc;
    }
    public int getOrder() {
        return order;
    }

    public String getDesc() {
        return desc;
    }
}
