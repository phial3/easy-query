package com.easy.query.core.expression.parser.impl;

import com.easy.query.core.context.QueryRuntimeContext;
import com.easy.query.core.exception.EasyQueryException;
import com.easy.query.core.expression.lambda.Property;
import com.easy.query.core.expression.parser.core.available.TableAvailable;
import com.easy.query.core.expression.segment.ColumnSegmentImpl;
import com.easy.query.core.expression.segment.SQLEntityAliasSegment;
import com.easy.query.core.expression.segment.SQLEntitySegment;
import com.easy.query.core.expression.segment.SQLSegment;
import com.easy.query.core.expression.segment.builder.SQLBuilderSegment;
import com.easy.query.core.expression.sql.builder.AnonymousEntityTableExpressionBuilder;
import com.easy.query.core.expression.sql.builder.EntityExpressionBuilder;
import com.easy.query.core.expression.sql.builder.EntityQueryExpressionBuilder;
import com.easy.query.core.expression.sql.builder.EntityTableExpressionBuilder;
import com.easy.query.core.expression.sql.builder.SQLAnonymousUnionEntityQueryExpressionBuilder;
import com.easy.query.core.util.EasyClassUtil;
import com.easy.query.core.util.EasyCollectionUtil;
import com.easy.query.core.util.EasyUtil;
import com.easy.query.core.util.EasyLambdaUtil;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @Description: 文件说明
 * @Date: 2023/2/8 12:26
 * @author xuejiaming
 */
public class AbstractSQLColumnSelector<T1, TChain> {
    protected final int index;
    protected final EntityExpressionBuilder entityExpressionBuilder;
    protected final TableAvailable table;
    protected final SQLBuilderSegment sqlSegmentBuilder;
    protected final QueryRuntimeContext runtimeContext;

    public AbstractSQLColumnSelector(int index, EntityExpressionBuilder entityExpressionBuilder, SQLBuilderSegment sqlSegmentBuilder) {
        this.index = index;

        this.entityExpressionBuilder = entityExpressionBuilder;
        this.runtimeContext=entityExpressionBuilder.getRuntimeContext();
        this.table=entityExpressionBuilder.getTable(index).getEntityTable();
        this.sqlSegmentBuilder = sqlSegmentBuilder;
    }


    public QueryRuntimeContext getRuntimeContext() {
        return runtimeContext;
    }

    public TChain column(Property<T1, ?> column) {
        EntityTableExpressionBuilder table = entityExpressionBuilder.getTable(index);
        String propertyName = EasyLambdaUtil.getPropertyName(column);
        sqlSegmentBuilder.append(new ColumnSegmentImpl(table.getEntityTable(), propertyName, entityExpressionBuilder.getRuntimeContext()));
        return (TChain) this;
    }


    public TChain columnIgnore(Property<T1, ?> column) {
        EntityTableExpressionBuilder table = entityExpressionBuilder.getTable(index);
        String propertyName = EasyLambdaUtil.getPropertyName(column);
        sqlSegmentBuilder.getSQLSegments().removeIf(sqlSegment -> {
            if (sqlSegment instanceof SQLEntitySegment) {
                SQLEntitySegment sqlEntitySegment = (SQLEntitySegment) sqlSegment;
                return Objects.equals(sqlEntitySegment.getTable(), table.getEntityTable()) && Objects.equals(sqlEntitySegment.getPropertyName(), propertyName);
            }
            return false;
        });
        return (TChain) this;
    }

    public TChain columnAll() {
        EntityTableExpressionBuilder table = entityExpressionBuilder.getTable(index);
        if (table instanceof AnonymousEntityTableExpressionBuilder) {
            columnAnonymousAll((AnonymousEntityTableExpressionBuilder) table);
        } else {
            Collection<String> properties = table.getEntityMetadata().getProperties();
            for (String property : properties) {
                sqlSegmentBuilder.append(new ColumnSegmentImpl(table.getEntityTable(), property, entityExpressionBuilder.getRuntimeContext()));
            }
        }
        return (TChain) this;
    }

    private EntityQueryExpressionBuilder getAnonymousTableQueryExpressionBuilder(AnonymousEntityTableExpressionBuilder table){
        EntityQueryExpressionBuilder entityQueryExpressionBuilder = table.getEntityQueryExpressionBuilder();
        if(entityQueryExpressionBuilder instanceof SQLAnonymousUnionEntityQueryExpressionBuilder){
            List<EntityQueryExpressionBuilder> entityQueryExpressionBuilders = ((SQLAnonymousUnionEntityQueryExpressionBuilder) entityQueryExpressionBuilder).getEntityQueryExpressionBuilders();
            return EasyCollectionUtil.first(entityQueryExpressionBuilders);
        }
        return entityQueryExpressionBuilder;
    }
    protected TChain columnAnonymousAll(AnonymousEntityTableExpressionBuilder table){
        EntityQueryExpressionBuilder sqlEntityQueryExpression = getAnonymousTableQueryExpressionBuilder(table);
        List<SQLSegment> sqlSegments = sqlEntityQueryExpression.getProjects().getSQLSegments();
        for (SQLSegment sqlSegment : sqlSegments) {
            if (sqlSegment instanceof SQLEntityAliasSegment) {
                SQLEntityAliasSegment sqlEntityAliasSegment = (SQLEntityAliasSegment) sqlSegment;
                String propertyName = EasyUtil.getAnonymousPropertyName(sqlEntityAliasSegment,table.getEntityTable());
                sqlSegmentBuilder.append(new ColumnSegmentImpl(table.getEntityTable(),propertyName , entityExpressionBuilder.getRuntimeContext(),sqlEntityAliasSegment.getAlias()));
            } else {
                throw new EasyQueryException("columnAll函数无法获取指定列" + EasyClassUtil.getInstanceSimpleName(sqlSegment));
            }
        }
        return (TChain) this;
    }


    public EntityExpressionBuilder getEntityExpressionBuilder() {
        return entityExpressionBuilder;
    }
}
