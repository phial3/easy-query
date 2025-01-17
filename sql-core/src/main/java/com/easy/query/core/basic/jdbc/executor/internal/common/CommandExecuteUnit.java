package com.easy.query.core.basic.jdbc.executor.internal.common;

import com.easy.query.core.basic.jdbc.con.EasyConnection;
import com.easy.query.core.enums.sharding.ConnectionModeEnum;

/**
 * create time 2023/4/13 22:05
 * 文件说明
 *
 * @author xuejiaming
 */
public final class CommandExecuteUnit {
    private final ExecutionUnit executionUnit;
    private final EasyConnection easyConnection;
    private final ConnectionModeEnum connectionMode;

    public CommandExecuteUnit(ExecutionUnit executionUnit, EasyConnection easyConnection, ConnectionModeEnum connectionMode) {
        this.executionUnit = executionUnit;
        this.easyConnection = easyConnection;
        this.connectionMode = connectionMode;
    }

    public ExecutionUnit getExecutionUnit() {
        return executionUnit;
    }


    public ConnectionModeEnum getConnectionMode() {
        return connectionMode;
    }

    public EasyConnection getEasyConnection() {
        return easyConnection;
    }
}
