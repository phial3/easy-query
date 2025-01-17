package com.easy.query.core.expression.segment.condition.predicate;

import com.easy.query.core.context.QueryRuntimeContext;
import com.easy.query.core.basic.jdbc.parameter.ToSQLContext;
import com.easy.query.core.expression.parser.core.available.TableAvailable;
import com.easy.query.core.expression.segment.SQLEntitySegment;
import com.easy.query.core.enums.SQLPredicateCompare;
import com.easy.query.core.util.EasySQLExpressionUtil;

/**
 * @FileName: ColumnValuePredicate.java
 * @Description: 文件说明
 * @Date: 2023/2/14 23:34
 * @author xuejiaming
 */
public class ColumnPredicate implements Predicate {
    private final TableAvailable table;
    private final String propertyName;
    private final SQLPredicateCompare compare;
    private final QueryRuntimeContext runtimeContext;

    public ColumnPredicate(TableAvailable table, String propertyName, SQLPredicateCompare compare, QueryRuntimeContext runtimeContext) {
        this.table = table;
        this.propertyName = propertyName;
        this.compare = compare;
        this.runtimeContext = runtimeContext;
    }

    @Override
    public String toSQL(ToSQLContext sqlParameterCollector) {
        String sqlColumnSegment = EasySQLExpressionUtil.getSQLOwnerColumn(runtimeContext,table,propertyName);
        return sqlColumnSegment +" "+ compare.getSQL();
    }

    @Override
    public TableAvailable getTable() {
        return table;
    }

    @Override
    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public SQLEntitySegment cloneSQLEntitySegment() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SQLPredicateCompare getOperator() {
        return compare;
    }
}
