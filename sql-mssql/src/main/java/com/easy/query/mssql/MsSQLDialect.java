package com.easy.query.mssql;


import com.easy.query.core.configuration.dialect.AbstractDialect;

/**
 * create time 2023/5/4 08:30
 * sql server 方言
 *
 * @author xuejiaming
 */
public final class MsSQLDialect extends AbstractDialect {
    @Override
    protected String getQuoteStart() {
        return "[";
    }

    @Override
    protected String getQuoteEnd() {
        return "]";
    }
}
