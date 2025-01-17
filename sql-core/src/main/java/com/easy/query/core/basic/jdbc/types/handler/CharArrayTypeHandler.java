package com.easy.query.core.basic.jdbc.types.handler;

import com.easy.query.core.basic.jdbc.types.EasyParameter;
import com.easy.query.core.basic.jdbc.types.EasyResultSet;

import java.sql.SQLException;
import java.sql.Types;

/**
 * @FileName: CharArrayTypeHandler.java
 * @Description: 文件说明
 * @Date: 2023/2/17 21:53
 * @author xuejiaming
 */
public class CharArrayTypeHandler implements JdbcTypeHandler {
    @Override
    public Object getValue(EasyResultSet resultSet) throws SQLException {
        return resultSet.getStreamResult().getString(resultSet.getIndex()).toCharArray();
    }

    @Override
    public void setParameter(EasyParameter parameter) throws SQLException {
        String str = new String((char[]) parameter.getValue());
        parameter.getPs().setString(parameter.getIndex(),str);
    }

    @Override
    public int getJdbcType() {
        return Types.CLOB;
    }
}
