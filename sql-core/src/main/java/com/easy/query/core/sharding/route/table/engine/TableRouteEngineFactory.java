package com.easy.query.core.sharding.route.table.engine;

import com.easy.query.core.expression.sql.builder.EntityQueryExpressionBuilder;
import com.easy.query.core.sharding.route.ShardingRouteResult;
import com.easy.query.core.sharding.route.datasource.engine.DataSourceRouteResult;

import java.util.Map;

/**
 * create time 2023/4/5 22:42
 * 文件说明
 *
 * @author xuejiaming
 */
public interface TableRouteEngineFactory {

    ShardingRouteResult route(DataSourceRouteResult dataSourceRouteResult, EntityQueryExpressionBuilder entityQueryExpression, Map<Class<?>, EntityQueryExpressionBuilder> queryEntities);
}
