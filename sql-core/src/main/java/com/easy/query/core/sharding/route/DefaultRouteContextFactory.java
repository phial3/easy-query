package com.easy.query.core.sharding.route;

import com.easy.query.core.exception.EasyQueryInvalidOperationException;
import com.easy.query.core.expression.executor.parser.EasyEntityPrepareParseResult;
import com.easy.query.core.expression.executor.parser.EntityPrepareParseResult;
import com.easy.query.core.expression.executor.parser.PredicatePrepareParseResult;
import com.easy.query.core.expression.executor.parser.PrepareParseResult;
import com.easy.query.core.expression.executor.parser.QueryPrepareParseResult;
import com.easy.query.core.expression.executor.parser.SequenceParseResult;
import com.easy.query.core.expression.executor.parser.descriptor.TableEntityParseDescriptor;
import com.easy.query.core.expression.executor.parser.descriptor.TableParseDescriptor;
import com.easy.query.core.expression.executor.parser.descriptor.TablePredicateParseDescriptor;
import com.easy.query.core.expression.executor.parser.descriptor.impl.TableEntityParseDescriptorImpl;
import com.easy.query.core.expression.executor.parser.descriptor.impl.TablePredicateParseDescriptorImpl;
import com.easy.query.core.expression.parser.core.available.TableAvailable;
import com.easy.query.core.expression.segment.condition.PredicateSegment;
import com.easy.query.core.sharding.route.datasource.engine.DataSourceRouteEngine;
import com.easy.query.core.sharding.route.datasource.engine.DataSourceRouteResult;
import com.easy.query.core.sharding.route.descriptor.PredicateRouteDescriptorImpl;
import com.easy.query.core.sharding.route.descriptor.RouteDescriptor;
import com.easy.query.core.sharding.route.table.EasyEntityTableRouteUnit;
import com.easy.query.core.sharding.route.table.TableRouteUnit;
import com.easy.query.core.sharding.route.table.engine.TableRouteContext;
import com.easy.query.core.sharding.route.table.engine.TableRouteEngine;
import com.easy.query.core.util.EasyCollectionUtil;
import com.easy.query.core.util.EasyClassUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * create time 2023/4/20 13:13
 * 文件说明
 *
 * @author xuejiaming
 */
public class DefaultRouteContextFactory implements RouteContextFactory {
    private final DataSourceRouteEngine dataSourceRouteEngine;
    private final TableRouteEngine tableRouteEngine;

    public DefaultRouteContextFactory(DataSourceRouteEngine dataSourceRouteEngine, TableRouteEngine tableRouteEngine) {

        this.dataSourceRouteEngine = dataSourceRouteEngine;
        this.tableRouteEngine = tableRouteEngine;
    }

    @Override
    public RouteContext createRouteContext(PrepareParseResult prepareParseResult) {
        if (prepareParseResult instanceof PredicatePrepareParseResult) {
            PredicatePrepareParseResult predicatePrepareParseResult = (PredicatePrepareParseResult) prepareParseResult;
            SequenceParseResult sequenceParseResult = (predicatePrepareParseResult instanceof QueryPrepareParseResult) ? ((QueryPrepareParseResult) predicatePrepareParseResult).getSequenceParseResult() : null;
            return createRouteContextByPredicate(predicatePrepareParseResult.getTableParseDescriptor(),sequenceParseResult);
        }
        if (prepareParseResult instanceof EntityPrepareParseResult) {
            EntityPrepareParseResult entityPrepareParseResult = (EntityPrepareParseResult) prepareParseResult;
            return createRouteContextByEntity(entityPrepareParseResult.getTableParseDescriptor());
        }
        throw new UnsupportedOperationException(EasyClassUtil.getInstanceSimpleName(prepareParseResult));
    }

    private RouteContext createRouteContextByPredicate(TableParseDescriptor tableParseDescriptor,SequenceParseResult sequenceParseResult) {
        return doCreateRouteContext(tableParseDescriptor,sequenceParseResult);
    }

    private RouteContext createRouteContextByEntity(TableEntityParseDescriptor tableEntityParseDescriptor) {
        TableAvailable table = EasyCollectionUtil.first(tableEntityParseDescriptor.getTables());
        List<Object> entities = tableEntityParseDescriptor.getEntitiesNotNull(table);
        ArrayList<RouteUnit> entityRouteUnits = new ArrayList<>(entities.size());
        String dataSource = null;
        String tableName = null;
        boolean isCrossDataSource = false;
        boolean isCrossTable = false;
        for (Object entity : entities) {
            TableEntityParseDescriptorImpl entityParseDescriptor = new TableEntityParseDescriptorImpl(table, Collections.singletonList(entity));
            RouteContext routeContext = doCreateRouteContext(entityParseDescriptor,null);
            List<RouteUnit> routeUnits = routeContext.getShardingRouteResult().getRouteUnits();
            if (EasyCollectionUtil.isNotSingle(routeUnits)) {
                throw new EasyQueryInvalidOperationException("entity route route unit more or empty:"+routeUnits.size());
            }
            RouteUnit routeUnit = routeUnits.get(0);
            List<TableRouteUnit> tableRouteUnits = routeUnit.getTableRouteUnits();

            if (EasyCollectionUtil.isNotSingle(tableRouteUnits)) {
                throw new EasyQueryInvalidOperationException("entity route table route unit more or empty:"+tableRouteUnits.size());
            }
            TableRouteUnit tableRouteUnit = tableRouteUnits.get(0);
            //判断是否存在跨datasource或者跨表的操作
            if (EasyCollectionUtil.isEmpty(entityRouteUnits)) {
                dataSource = routeUnit.getDataSource();
                tableName = routeUnit.getTableRouteUnits().get(0).getActualTableName();
            }
            String currentDataSource = routeUnit.getDataSource();
            String currentTableName = tableRouteUnit.getActualTableName();
            if (!isCrossDataSource) {
                isCrossDataSource = !Objects.equals(dataSource, currentDataSource);
            }
            if (!isCrossTable) {
                isCrossTable = !Objects.equals(tableName, currentTableName);
            }
            EasyEntityTableRouteUnit easyEntityTableRouteUnit = new EasyEntityTableRouteUnit(tableRouteUnit, entity);
            RouteUnit entityRouteUint = new RouteUnit(currentDataSource, Collections.singletonList(easyEntityTableRouteUnit));
            entityRouteUnits.add(entityRouteUint);
        }
        ShardingRouteResult shardingRouteResult = new ShardingRouteResult(entityRouteUnits, isCrossDataSource, isCrossTable,false);
        return new RouteContext(shardingRouteResult);

    }

    private RouteContext doCreateRouteContext(TableParseDescriptor tableParseDescriptor, SequenceParseResult sequenceParseResult) {
        //获取分库节点
        DataSourceRouteResult dataSourceRouteResult = dataSourceRouteEngine.route(tableParseDescriptor);

        //获取分片后的结果
        ShardingRouteResult shardingRouteResult = tableRouteEngine.route(new TableRouteContext(dataSourceRouteResult, tableParseDescriptor,sequenceParseResult));
//        tableRouteEngine.route()
        return new RouteContext(shardingRouteResult);
    }
}
