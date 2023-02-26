package org.easy.query.core.basic.expression.parser.impl;

import org.easy.query.core.impl.SelectContext;

/**
 * @FileName: DefaultSqlColumnSelector.java
 * @Description: 文件说明
 * @Date: 2023/2/12 21:36
 * @Created by xuejiaming
 */
public class DefaultSqlGroupColumnSelector<T1> extends DefaultSqlColumnSelector<T1> {
    public DefaultSqlGroupColumnSelector(int index, SelectContext selectContext) {
        super(index, selectContext,selectContext.getGroup());
    }
}