package org.easy.query.core.impl;

import org.easy.query.core.abstraction.*;
import org.easy.query.core.basic.sql.segment.builder.SelectSqlSegmentBuilder;
import org.easy.query.core.basic.sql.segment.builder.SqlSegmentBuilder;
import org.easy.query.core.basic.sql.segment.builder.UpdateSetSqlSegmentBuilder;
import org.easy.query.core.query.builder.SqlTableInfo;
import org.easy.query.core.basic.sql.segment.segment.AndPredicateSegment;
import org.easy.query.core.basic.sql.segment.segment.PredicateSegment;

import java.util.ArrayList;
import java.util.List;

/**
 * @FileName: UpdateContext.java
 * @Description: 文件说明
 * @Date: 2023/2/24 22:07
 * @Created by xuejiaming
 */
public class UpdateContext extends AbstractSqlPredicateContext{

    private final SqlSegmentBuilder setColumns;
    private final PredicateSegment where;
    private SqlSegmentBuilder setIgnoreColumns;
    private SqlSegmentBuilder whereColumns;
    private final List<Object> parameters;

    public UpdateContext(EasyQueryRuntimeContext runtimeContext,boolean expressionUpdate) {
        super(runtimeContext);
        setColumns =new UpdateSetSqlSegmentBuilder();
        where=new AndPredicateSegment(true);
        parameters=expressionUpdate?new ArrayList<>():null;
    }
    public void addSqlTable(SqlTableInfo sqlTableInfo){
        this.tables.add(sqlTableInfo);
    }

    public SqlSegmentBuilder getSetColumns() {
        return setColumns;
    }

    public PredicateSegment getWhere() {
        return where;
    }
    public SqlSegmentBuilder getSetIgnoreColumns(){
        if(setIgnoreColumns==null){
            setIgnoreColumns=new UpdateSetSqlSegmentBuilder();
        }
        return setIgnoreColumns;
    }
    public SqlSegmentBuilder getWhereColumns(){
        if(whereColumns==null){
            whereColumns=new SelectSqlSegmentBuilder();
        }
        return whereColumns;
    }

    @Override
    public List<Object> getParameters() {
        return parameters;
    }

    @Override
    public void addParameter(Object parameter) {
        parameters.add(parameter);
    }
}