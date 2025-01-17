package com.easy.query.core.basic.api.update;

import com.easy.query.core.basic.api.internal.SQLExecuteStrategy;
import com.easy.query.core.expression.lambda.SQLExpression1;
import com.easy.query.core.expression.parser.core.SQLColumnSelector;

/**
 * @author xuejiaming
 * @FileName: EntityUpdate.java
 * @Description: 文件说明
 * @Date: 2023/2/24 23:22
 */
public interface EntityUpdatable<T> extends Updatable<T, EntityUpdatable<T>>, SQLExecuteStrategy<EntityUpdatable<T>> {
    default EntityUpdatable<T> setColumns(SQLExpression1<SQLColumnSelector<T>> columnSelectorExpression) {
        return setColumns(true, columnSelectorExpression);
    }

    EntityUpdatable<T> setColumns(boolean condition, SQLExpression1<SQLColumnSelector<T>> columnSelectorExpression);

    default EntityUpdatable<T> setIgnoreColumns(SQLExpression1<SQLColumnSelector<T>> columnSelectorExpression) {
        return setIgnoreColumns(true, columnSelectorExpression);
    }

    EntityUpdatable<T> setIgnoreColumns(boolean condition, SQLExpression1<SQLColumnSelector<T>> columnSelectorExpression);

    default EntityUpdatable<T> whereColumns(SQLExpression1<SQLColumnSelector<T>> columnSelectorExpression) {
        return whereColumns(true, columnSelectorExpression);
    }

    EntityUpdatable<T> whereColumns(boolean condition, SQLExpression1<SQLColumnSelector<T>> columnSelectorExpression);

}
