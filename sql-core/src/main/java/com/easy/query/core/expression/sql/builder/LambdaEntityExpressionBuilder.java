package com.easy.query.core.expression.sql.builder;

import com.easy.query.core.expression.sql.builder.EntityExpressionBuilder;

/**
 * create time 2023/3/27 22:04
 * 文件说明
 *
 * @author xuejiaming
 */
public interface LambdaEntityExpressionBuilder extends EntityExpressionBuilder {
    /**
     * 是否是表达式sql以下几个方法为true:select、update expression、delete expression
     * @return
     */
    boolean isExpression();
}
