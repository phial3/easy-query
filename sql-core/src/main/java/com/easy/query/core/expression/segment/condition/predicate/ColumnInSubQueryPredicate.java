package com.easy.query.core.expression.segment.condition.predicate;

import com.easy.query.core.context.QueryRuntimeContext;
import com.easy.query.core.basic.api.select.Queryable;
import com.easy.query.core.basic.jdbc.parameter.ToSQLContext;
import com.easy.query.core.enums.SQLPredicateCompare;
import com.easy.query.core.expression.parser.core.available.TableAvailable;
import com.easy.query.core.expression.segment.SQLEntitySegment;
import com.easy.query.core.util.EasySQLExpressionUtil;

/**
 * create time 2023/4/27 09:22
 * 文件说明
 *
 * @author xuejiaming
 */
public class ColumnInSubQueryPredicate implements SubQueryPredicate{
    private final SQLPredicateCompare compare;
    private final QueryRuntimeContext runtimeContext;
    private final TableAvailable table;
    private final String propertyName;
    private final Queryable<?> subQueryable;

    public ColumnInSubQueryPredicate(TableAvailable table, String propertyName, Queryable<?> subQueryable, SQLPredicateCompare compare, QueryRuntimeContext runtimeContext) {
        this.table = table;
        this.propertyName = propertyName;
        this.subQueryable = subQueryable;
        this.compare = compare;
        this.runtimeContext = runtimeContext;
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
    public String toSQL(ToSQLContext sqlParameterCollector) {

        String sqlColumnSegment = EasySQLExpressionUtil.getSQLOwnerColumn(runtimeContext,table,propertyName);
        StringBuilder sql = new StringBuilder();
        sql.append(sqlColumnSegment).append(" ").append(compare.getSQL()).append(" (");
        String subQueryableSQL = subQueryable.toSQL(sqlParameterCollector);
        sql.append(subQueryableSQL).append(") ");
        return sql.toString();
    }

    @Override
    public SQLPredicateCompare getOperator() {
        return compare;
    }

    @Override
    public Queryable<?> getSubQueryable() {
        return subQueryable;
    }

    @Override
    public SubQueryPredicate cloneSubQueryPredicate() {
        return new ColumnInSubQueryPredicate(table,propertyName,subQueryable.cloneQueryable(),compare,runtimeContext);
    }
}
