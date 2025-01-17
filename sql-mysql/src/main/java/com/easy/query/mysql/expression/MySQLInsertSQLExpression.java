package com.easy.query.mysql.expression;

import com.easy.query.core.context.QueryRuntimeContext;
import com.easy.query.core.expression.sql.expression.EntityTableSQLExpression;
import com.easy.query.core.expression.sql.expression.impl.InsertSQLExpressionImpl;

/**
 * create time 2023/5/17 22:36
 * 文件说明
 *
 * @author xuejiaming
 */
public class MySQLInsertSQLExpression extends InsertSQLExpressionImpl {
    public MySQLInsertSQLExpression(QueryRuntimeContext runtimeContext, EntityTableSQLExpression table) {
        super(runtimeContext, table);
    }
}
