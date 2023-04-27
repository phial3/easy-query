package com.easy.query.core.basic.jdbc.executor.internal;

import com.easy.query.core.basic.jdbc.executor.internal.abstraction.AbstractEasyQueryJdbcExecutor;
import com.easy.query.core.sharding.merge.StreamMergeContext;
import com.easy.query.core.basic.jdbc.executor.internal.unit.EasyQueryExecutor;
import com.easy.query.core.basic.jdbc.executor.internal.unit.Executor;

/**
 * create time 2023/4/21 08:28
 * 文件说明
 *
 * @author xuejiaming
 */
public class DefaultQueryEasyQueryJdbcExecutor extends AbstractEasyQueryJdbcExecutor<QueryExecuteResult> {
    public DefaultQueryEasyQueryJdbcExecutor(StreamMergeContext streamMergeContext) {
        super(streamMergeContext);
    }

    @Override
    protected Executor<QueryExecuteResult> createExecutor() {
        return new EasyQueryExecutor(streamMergeContext);
    }

    @Override
    protected QueryExecuteResult defaultResult() {
        return QueryExecuteResult.empty();
    }
}