package com.easy.query.core.expression.segment;

import com.easy.query.core.context.QueryRuntimeContext;
import com.easy.query.core.basic.jdbc.parameter.EasyConstSQLParameter;
import com.easy.query.core.basic.jdbc.parameter.ToSQLContext;
import com.easy.query.core.enums.SQLPredicateCompare;
import com.easy.query.core.expression.parser.core.available.TableAvailable;
import com.easy.query.core.util.EasySQLUtil;
import com.easy.query.core.util.EasySQLExpressionUtil;

/**
 * @FileName: ColumnValuePredicate.java
 * @Description: 文件说明
 * @Date: 2023/2/14 23:34
 * @author xuejiaming
 */
public class ColumnWithSelfSegment implements SQLEntitySegment {
    private static final String INCREMENT="+?";
    private static final String DECREMENT="-?";
    private final String propertyName;
    private final Object val;
    private final SQLPredicateCompare compare;
    private final QueryRuntimeContext runtimeContext;
    private final String selfLink;
    private final TableAvailable entityTable;

    public ColumnWithSelfSegment(boolean increment, TableAvailable entityTable, String propertyName, Object val, SQLPredicateCompare compare, QueryRuntimeContext runtimeContext) {
        this.selfLink=increment?INCREMENT:DECREMENT;
        this.entityTable = entityTable;
        this.propertyName = propertyName;
        this.val = val;
        this.compare = compare;
        this.runtimeContext = runtimeContext;
    }

    @Override
    public String toSQL(ToSQLContext sqlParameterCollector) {
        EasySQLUtil.addParameter(sqlParameterCollector,new EasyConstSQLParameter(entityTable,propertyName,val));
        String sqlColumnSegment1 = EasySQLExpressionUtil.getSQLOwnerColumn(runtimeContext,entityTable,propertyName);
        return sqlColumnSegment1 +" "+ compare.getSQL() + " "+sqlColumnSegment1+selfLink;
    }

    @Override
    public TableAvailable getTable() {
        return entityTable;
    }

    @Override
    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public SQLEntitySegment cloneSQLEntitySegment() {
        throw new UnsupportedOperationException();
    }
}
