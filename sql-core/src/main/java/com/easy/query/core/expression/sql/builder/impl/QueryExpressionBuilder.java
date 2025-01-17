package com.easy.query.core.expression.sql.builder.impl;

import com.easy.query.core.context.QueryRuntimeContext;
import com.easy.query.core.exception.EasyQueryException;
import com.easy.query.core.expression.segment.SelectConstSegment;
import com.easy.query.core.expression.segment.builder.GroupBySQLBuilderSegmentImpl;
import com.easy.query.core.expression.segment.builder.OrderBySQLBuilderSegmentImpl;
import com.easy.query.core.expression.segment.builder.ProjectSQLBuilderSegmentImpl;
import com.easy.query.core.expression.segment.builder.SQLBuilderSegment;
import com.easy.query.core.expression.segment.condition.AndPredicateSegment;
import com.easy.query.core.expression.segment.condition.PredicateSegment;
import com.easy.query.core.expression.sql.builder.AnonymousEntityTableExpressionBuilder;
import com.easy.query.core.expression.sql.builder.SQLEntityQueryExpressionBuilder;
import com.easy.query.core.expression.sql.expression.EntityQuerySQLExpression;
import com.easy.query.core.expression.sql.expression.SQLExpression;
import com.easy.query.core.expression.sql.expression.EntityTableSQLExpression;
import com.easy.query.core.expression.sql.builder.internal.AbstractPredicateEntityExpressionBuilder;
import com.easy.query.core.expression.sql.builder.EntityQueryExpressionBuilder;
import com.easy.query.core.expression.sql.builder.EntityTableExpressionBuilder;
import com.easy.query.core.expression.sql.builder.ExpressionContext;
import com.easy.query.core.expression.sql.expression.factory.ExpressionFactory;
import com.easy.query.core.util.EasySQLSegmentUtil;

import java.util.Iterator;

/**
 * @author xuejiaming
 * @Description: 文件说明
 * @Date: 2023/3/3 22:10
 */
public class QueryExpressionBuilder extends AbstractPredicateEntityExpressionBuilder implements EntityQueryExpressionBuilder {

    protected PredicateSegment where;
    protected PredicateSegment allPredicate;
    protected SQLBuilderSegment group;
    protected PredicateSegment having;
    protected SQLBuilderSegment order;
    protected long offset;
    protected long rows;
    protected boolean distinct;

    protected final SQLBuilderSegment projects;

    public QueryExpressionBuilder(ExpressionContext expressionContext) {
        super(expressionContext);
        this.projects = new ProjectSQLBuilderSegmentImpl();
    }


    @Override
    public boolean isEmpty() {
        return tables.isEmpty();
    }

    @Override
    public SQLBuilderSegment getProjects() {
        return projects;
    }

    @Override
    public PredicateSegment getWhere() {
        if (where == null) {
            where = new AndPredicateSegment(true);
        }
        return where;
    }

    @Override
    public boolean hasAllPredicate() {
        return EasySQLSegmentUtil.isNotEmpty(allPredicate);
    }

    @Override
    public PredicateSegment getAllPredicate() {
        if (allPredicate == null) {
            allPredicate = new AndPredicateSegment(true);
        }
        return allPredicate;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public void setOffset(long offset) {
        this.offset = offset;
    }

    @Override
    public long getRows() {
        return rows;
    }

    @Override
    public void setRows(long rows) {
        this.rows = rows;
    }

    @Override
    public boolean hasLimit() {
        return this.rows > 0;
    }

    @Override
    public boolean hasWhere() {
        return where != null && where.isNotEmpty();
    }

    @Override
    public PredicateSegment getHaving() {
        if (having == null) {
            having = new AndPredicateSegment(true);
        }
        return having;
    }

    @Override
    public boolean hasHaving() {
        return EasySQLSegmentUtil.isNotEmpty(having);
    }

    @Override
    public SQLBuilderSegment getGroup() {
        if (group == null) {
            group = new GroupBySQLBuilderSegmentImpl();
        }
        return group;
    }

    @Override
    public boolean hasGroup() {
        return group != null && group.isNotEmpty();
    }

    @Override
    public SQLBuilderSegment getOrder() {
        if (order == null) {
            order = new OrderBySQLBuilderSegmentImpl();
        }
        return order;
    }

    @Override
    public boolean hasOrder() {
        return order != null && order.isNotEmpty();
    }


    @Override
    public boolean isDistinct() {
        return distinct;
    }

    @Override
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean isExpression() {
        return true;
    }

    @Override
    public EntityQuerySQLExpression toExpression() {
        int tableCount = getTables().size();
        if (tableCount == 0) {
            throw new EasyQueryException("未找到查询表信息");
        }
        boolean emptySelect = getProjects().isEmpty();
        Iterator<EntityTableExpressionBuilder> iterator = getTables().iterator();
        EntityTableExpressionBuilder firstTable = iterator.next();
        //如果有order需要将order移到内部表达式
        if (emptySelect && tableCount == 1 && (firstTable instanceof AnonymousEntityTableExpressionBuilder && !(((AnonymousEntityTableExpressionBuilder) firstTable).getEntityQueryExpressionBuilder() instanceof SQLEntityQueryExpressionBuilder))) {
            return (EntityQuerySQLExpression) toTableExpressionSQL(firstTable, true);
        }
        QueryRuntimeContext runtimeContext = getRuntimeContext();
        ExpressionFactory expressionFactory = runtimeContext.getExpressionFactory();
        EntityQuerySQLExpression easyQuerySQLExpression = expressionFactory.createEasyQuerySQLExpression(runtimeContext);
        easyQuerySQLExpression.setDistinct(isDistinct());

        if (emptySelect) {
            if (!hasGroup()) {
                ProjectSQLBuilderSegmentImpl projects = new ProjectSQLBuilderSegmentImpl();
                projects.append(new SelectConstSegment(firstTable.getAlias() + ".*"));
                easyQuerySQLExpression.setProjects(projects);
            } else {
                ProjectSQLBuilderSegmentImpl projects = new ProjectSQLBuilderSegmentImpl();
                getGroup().copyTo(projects);
                easyQuerySQLExpression.setProjects(projects);
            }
        } else {
            easyQuerySQLExpression.setProjects(getProjects());
        }
        easyQuerySQLExpression.getTables().add((EntityTableSQLExpression) toTableExpressionSQL(firstTable, false));
        while (iterator.hasNext()) {
            EntityTableExpressionBuilder table = iterator.next();
            EntityTableSQLExpression tableExpression = (EntityTableSQLExpression) toTableExpressionSQL(table, false);
            easyQuerySQLExpression.getTables().add(tableExpression);
            PredicateSegment on = getTableOnWithQueryFilter(table);
            if (on != null && on.isNotEmpty()) {
                tableExpression.setOn(on);
            }
        }
        PredicateSegment where = getSQLWhereWithQueryFilter();
        if (where != null && where.isNotEmpty()) {
            easyQuerySQLExpression.setWhere(where);
        }
        easyQuerySQLExpression.setGroup(getGroup());
        easyQuerySQLExpression.setHaving(getHaving());
        easyQuerySQLExpression.setOrder(getOrder());
        easyQuerySQLExpression.setOffset(getOffset());
        easyQuerySQLExpression.setRows(getRows());
        easyQuerySQLExpression.setAllPredicate(getAllPredicate());
        return easyQuerySQLExpression;
    }

    protected SQLExpression toTableExpressionSQL(EntityTableExpressionBuilder entityTableExpressionBuilder, boolean onlySingleAnonymousTable) {
        if (entityTableExpressionBuilder instanceof AnonymousEntityTableExpressionBuilder) {

            EntityQueryExpressionBuilder sqlEntityQueryExpression = ((AnonymousEntityTableExpressionBuilder) entityTableExpressionBuilder).getEntityQueryExpressionBuilder();
            //如果只有单匿名表且未对齐select那么嵌套表需要被展开
            //todo 如果对其进行order 或者 where了呢怎么办
            return onlySingleAnonymousTable ? sqlEntityQueryExpression.toExpression() : entityTableExpressionBuilder.toExpression();
        }
        return entityTableExpressionBuilder.toExpression();
    }

    protected PredicateSegment getTableOnWithQueryFilter(EntityTableExpressionBuilder table) {
        return sqlPredicateFilter(table, table.hasOn() ? table.getOn() : null);
    }

    protected PredicateSegment getSQLWhereWithQueryFilter() {
        EntityTableExpressionBuilder table = getTable(0);
        return sqlPredicateFilter(table, hasWhere() ? getWhere() : null);
    }


    @Override
    public EntityQueryExpressionBuilder cloneEntityExpressionBuilder() {

        EntityQueryExpressionBuilder queryExpressionBuilder = runtimeContext.getExpressionBuilderFactory().createEntityQueryExpressionBuilder(expressionContext);
        if (hasWhere()) {
            getWhere().copyTo(queryExpressionBuilder.getWhere());
        }
        if (hasGroup()) {
            getGroup().copyTo(queryExpressionBuilder.getGroup());
        }
        if (hasHaving()) {
            getHaving().copyTo(queryExpressionBuilder.getHaving());
        }
        if (hasOrder()) {
            getOrder().copyTo(queryExpressionBuilder.getOrder());
        }
        if(hasAllPredicate()){
            getAllPredicate().copyTo(queryExpressionBuilder.getAllPredicate());
        }
        getProjects().copyTo(queryExpressionBuilder.getProjects());
        queryExpressionBuilder.setOffset(this.offset);
        queryExpressionBuilder.setRows(this.rows);
        for (EntityTableExpressionBuilder table : super.tables) {
            queryExpressionBuilder.getTables().add(table.copyEntityTableExpressionBuilder());
        }
        return queryExpressionBuilder;
    }
}
