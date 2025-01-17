package com.easy.query.core.basic.plugin.logicdel.impl;

import com.easy.query.core.basic.enums.LogicDeleteStrategyEnum;
import com.easy.query.core.basic.plugin.logicdel.LogicDeleteBuilder;
import com.easy.query.core.basic.plugin.logicdel.abstraction.AbstractEasyLogicDeleteStrategy;
import com.easy.query.core.expression.lambda.Property;
import com.easy.query.core.expression.lambda.SQLExpression1;
import com.easy.query.core.expression.parser.core.SQLWherePredicate;
import com.easy.query.core.expression.parser.core.SQLColumnSetter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @FileName: TimestampGlobalEntityTypeConfiguration.java
 * @Description: 文件说明
 * @Date: 2023/3/6 22:45
 * @author xuejiaming
 */
public class DeleteLongTimestampEasyEntityTypeConfiguration extends AbstractEasyLogicDeleteStrategy {
    private static final Set<Class<?>> allowedPropertyTypes =new HashSet<>(Arrays.asList(Long.class,long.class));
    @Override
    public String getStrategy() {
        return LogicDeleteStrategyEnum.DELETE_LONG_TIMESTAMP.getStrategy();
    }

    @Override
    public Set<Class<?>> allowedPropertyTypes() {
        return allowedPropertyTypes;
    }


    @Override
    protected SQLExpression1<SQLWherePredicate<Object>> getPredicateFilterExpression(LogicDeleteBuilder builder, Property<Object,?> lambdaProperty) {
        return o->o.eq(lambdaProperty,0);
    }

    @Override
    protected SQLExpression1<SQLColumnSetter<Object>> getDeletedSQLExpression(LogicDeleteBuilder builder, Property<Object,?> lambdaProperty) {
        return o->o.set(lambdaProperty, System.currentTimeMillis());
    }
}
