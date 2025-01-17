package com.easy.query.core.expression.segment.builder;

import com.easy.query.core.basic.jdbc.parameter.ToSQLContext;
import com.easy.query.core.enums.SQLKeywordEnum;
import com.easy.query.core.expression.segment.AggregationColumnSegment;
import com.easy.query.core.expression.segment.SQLSegment;

import java.util.Iterator;
import java.util.List;

/**
 * @Description: 查询projects
 * @Date: 2023/2/13 22:39
 * @author xuejiaming
 */
public class ProjectSQLBuilderSegmentImpl extends AbstractSQLBuilderSegment implements ProjectSQLBuilderSegment {

    private boolean projectHasAggregate=false;
    @Override
    public String toSQL(ToSQLContext sqlParameterCollector) {
        StringBuilder sb = new StringBuilder();
        List<SQLSegment> sqlSegments = getSQLSegments();
        if (!sqlSegments.isEmpty()) {
            Iterator<SQLSegment> iterator = sqlSegments.iterator();
            SQLSegment first = iterator.next();
            sb.append(first.toSQL(sqlParameterCollector));
            while (iterator.hasNext()) {
                SQLSegment sqlSegment = iterator.next();
                sb.append(SQLKeywordEnum.DOT.toSQL()).append(sqlSegment.toSQL(sqlParameterCollector));
            }
        }
        return sb.toString();
    }

    @Override
    public SQLBuilderSegment cloneSQLBuilder() {
        ProjectSQLBuilderSegmentImpl projectSQLBuilderSegment = new ProjectSQLBuilderSegmentImpl();
        projectSQLBuilderSegment.projectHasAggregate=projectHasAggregate;
        copyTo(projectSQLBuilderSegment);
        return projectSQLBuilderSegment;
    }

    @Override
    public void append(SQLSegment sqlSegment) {
        super.append(sqlSegment);
        if(sqlSegment instanceof AggregationColumnSegment){
            projectHasAggregate=true;
        }
    }

    @Override
    public boolean hasAggregateColumns() {
        return projectHasAggregate;
    }
}
