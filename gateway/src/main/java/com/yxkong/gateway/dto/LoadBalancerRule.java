package com.yxkong.gateway.dto;

/**
 * 负载均衡规则
 *
 * @Author: yxkong
 * @Date: 2021/5/13 4:00 下午
 * @version: 1.0
 */
public class LoadBalancerRule {

    public LoadBalancerRule(String filed, String operator, String val, String union_relation) {
        this.filed = filed;
        this.operator = operator;
        this.val = val;
        this.union_relation = union_relation;
    }

    /**
     * 字段
     */
    private String filed;
    /**
     * 操作符，大于 gt,大于等于 gte,等于eq,小于le,小于等于lte,取模 mod10
     */
    private String operator;
    /**
     * 具体值
     */
    private String val;
    /**
     * 级联关系,and or
     */
    private String union_relation;

    public String getFiled() {
        return filed;
    }

    public void setFiled(String filed) {
        this.filed = filed;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getUnion_relation() {
        return union_relation;
    }

    public void setUnion_relation(String union_relation) {
        this.union_relation = union_relation;
    }
}