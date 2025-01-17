package com.easy.query.core.basic.jdbc.executor.internal.command.abstraction;

import com.easy.query.core.basic.jdbc.executor.internal.command.JdbcCommand;
import com.easy.query.core.sharding.context.StreamMergeContext;
import com.easy.query.core.basic.jdbc.executor.internal.ShardingExecutor;
import com.easy.query.core.basic.jdbc.executor.internal.common.ExecutionUnit;
import com.easy.query.core.basic.jdbc.executor.internal.result.ExecuteResult;
import com.easy.query.core.basic.jdbc.executor.internal.unit.Executor;
import com.easy.query.core.util.EasyCollectionUtil;

import java.sql.SQLException;
import java.util.List;

/**
 * create time 2023/4/21 08:26
 * 文件说明
 *
 * @author xuejiaming
 */
public abstract class AbstractJdbcCommand<T extends ExecuteResult> implements JdbcCommand<T> {
    protected final StreamMergeContext streamMergeContext;

    public AbstractJdbcCommand(StreamMergeContext streamMergeContext){

        this.streamMergeContext = streamMergeContext;
    }
    protected  abstract Executor<T> createExecutor();
    protected List<ExecutionUnit> getDefaultSQLRouteUnits(){
        return streamMergeContext.getExecutionUnits();
    }
    @Override
    public T execute() throws SQLException {
        List<ExecutionUnit> executionUnits = getDefaultSQLRouteUnits();
        if(EasyCollectionUtil.isEmpty(executionUnits)){
            return defaultResult();
        }
        Executor<T> executor = createExecutor();
        return ShardingExecutor.execute(streamMergeContext,executor,executionUnits);
    }
    protected abstract T defaultResult();

    @Override
    public void close() throws Exception {
        streamMergeContext.close();
    }
}
