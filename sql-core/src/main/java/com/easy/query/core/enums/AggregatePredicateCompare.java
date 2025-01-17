package com.easy.query.core.enums;

/**
 * @FileName: EasyAggregate.java
 * @Description: 文件说明
 * @Date: 2023/2/18 22:18
 * @author xuejiaming
 */
public enum AggregatePredicateCompare implements SQLPredicateCompare {
    EQ("="),
    NE("<>"),
    /**
     * 大于 >
     */
    GT(">"),
    GE(">="),
    LT("<"),
    LE("<=");
    private final String sql;

     AggregatePredicateCompare(String sql){

        this.sql = sql;
    }

    @Override
    public String getSQL() {
        return sql;
    }

    @Override
    public SQLPredicateCompare toReverse() {
        return null;
    }
}
