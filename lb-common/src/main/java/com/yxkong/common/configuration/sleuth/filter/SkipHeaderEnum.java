package com.yxkong.common.configuration.sleuth.filter;

import java.util.Arrays;

/**
 * @Author: yxkong
 * @Date: 2021/8/4 6:40 下午
 * @version: 1.0
 */
public enum SkipHeaderEnum {
    parentspanid("x-b3-parentspanid"),
    sampled("x-b3-sampled"),
    spanid("x-b3-spanid"),
    traceid("x-b3-traceid");
    private String key;
    private SkipHeaderEnum(String key){
        this.key = key;
    }

    public String getKey() {
        return key;
    }
    public static boolean contains(String uri){
        return Arrays.stream(SkipHeaderEnum.values()).anyMatch(s -> s.getKey().equalsIgnoreCase(uri));
    }
}
