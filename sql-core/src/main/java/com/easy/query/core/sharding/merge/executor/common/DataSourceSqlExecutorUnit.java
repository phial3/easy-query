package com.easy.query.core.sharding.merge.executor.common;

import com.easy.query.core.sharding.enums.ConnectionModeEnum;

import java.util.List;

/**
 * create time 2023/4/13 22:02
 * 文件说明
 *
 * @author xuejiaming
 */
public class DataSourceSqlExecutorUnit {
    private final ConnectionModeEnum connectionMode;
    private final List<SqlExecutorGroup<CommandExecuteUnit>> sqlExecutorGroups;

    public DataSourceSqlExecutorUnit(ConnectionModeEnum connectionMode, List<SqlExecutorGroup<CommandExecuteUnit>> sqlExecutorGroups) {
        this.connectionMode = connectionMode;
        this.sqlExecutorGroups = sqlExecutorGroups;
    }

    public ConnectionModeEnum getConnectionMode() {
        return connectionMode;
    }

    public List<SqlExecutorGroup<CommandExecuteUnit>> getSqlExecutorGroups() {
        return sqlExecutorGroups;
    }
}
