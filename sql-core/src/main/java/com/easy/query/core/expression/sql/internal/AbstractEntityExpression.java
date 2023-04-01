package com.easy.query.core.expression.sql.internal;

import com.easy.query.core.abstraction.EasyQueryRuntimeContext;
import com.easy.query.core.basic.jdbc.parameter.SQLParameter;
import com.easy.query.core.enums.EasyBehaviorEnum;
import com.easy.query.core.expression.sql.EntityExpression;
import com.easy.query.core.expression.sql.EntityTableExpression;
import com.easy.query.core.expression.sql.ExpressionContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @FileName: AbstractSqlEntityExpression.java
 * @Description: 文件说明
 * @Date: 2023/3/6 08:58
 * @author xuejiaming
 */
public abstract class AbstractEntityExpression implements EntityExpression {
    protected final ExpressionContext sqlExpressionContext;
    protected final List<EntityTableExpression> tables;

    public AbstractEntityExpression(ExpressionContext sqlExpressionContext){
        this.sqlExpressionContext = sqlExpressionContext;
        this.tables = new ArrayList<>();
    }

    @Override
    public ExpressionContext getExpressionContext() {
        return sqlExpressionContext;
    }

    @Override
    public EasyQueryRuntimeContext getRuntimeContext() {
        return sqlExpressionContext.getRuntimeContext();
    }

    @Override
    public void addSqlEntityTableExpression(EntityTableExpression tableExpression) {
        tables.add(tableExpression);
    }

    @Override
    public List<EntityTableExpression> getTables() {
        return tables;
    }

    @Override
    public EntityTableExpression getTable(int index) {
        return tables.get(index);
    }

    public String getQuoteName(String value) {
        return sqlExpressionContext.getQuoteName(value);
    }

    @Override
    public String getSqlOwnerColumn(EntityTableExpression table, String propertyName) {
        String alias = table.getAlias();
        String columnName = table.getColumnName(propertyName);
        String quoteName = getQuoteName(columnName);
        if (alias == null) {
            return quoteName;
        }
        return alias + "." + quoteName;
    }
    @Override
    public List<SQLParameter> getParameters() {
        return sqlExpressionContext.getParameters();
    }

    @Override
    public void addParameter(SQLParameter parameter) {
        sqlExpressionContext.addParameter(parameter);
    }
    @Override
    public void setLogicDelete(boolean logicDelete) {
        if(logicDelete){
            sqlExpressionContext.getBehavior().addBehavior(EasyBehaviorEnum.LOGIC_DELETE);
        }else{
            sqlExpressionContext.getBehavior().removeBehavior(EasyBehaviorEnum.LOGIC_DELETE);
        }
    }
}
