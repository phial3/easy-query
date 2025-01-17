package com.easy.query.core.basic.api.delete.impl;

import com.easy.query.core.basic.api.delete.abstraction.AbstractEntityDeletable;
import com.easy.query.core.expression.sql.builder.EntityDeleteExpressionBuilder;

import java.util.Collection;

/**
 * @FileName: EasyEntityDeletable.java
 * @Description: 文件说明
 * @Date: 2023/3/6 13:20
 * @author xuejiaming
 */
public class EasyEntityDeletable<T> extends AbstractEntityDeletable<T> {
    public EasyEntityDeletable(Collection<T> entities, EntityDeleteExpressionBuilder entityDeleteExpression) {
        super(entities, entityDeleteExpression);
    }
}
