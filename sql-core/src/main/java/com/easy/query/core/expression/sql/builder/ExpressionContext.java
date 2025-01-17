package com.easy.query.core.expression.sql.builder;

import com.easy.query.core.context.QueryRuntimeContext;
import com.easy.query.core.basic.plugin.interceptor.EasyInterceptorEntry;
import com.easy.query.core.enums.ExecuteMethodEnum;
import com.easy.query.core.enums.SQLExecuteStrategyEnum;
import com.easy.query.core.expression.sql.builder.internal.EasyBehavior;
import com.easy.query.core.enums.sharding.ConnectionModeEnum;

import java.util.List;
import java.util.stream.Stream;

/**
 * @FileName: QueryExpressionContext.java
 * @Description: 文件说明
 * @Date: 2023/3/3 23:05
 * @author xuejiaming
 */
public interface ExpressionContext {
    QueryRuntimeContext getRuntimeContext();

//     List<SQLParameter> getParameters();
//     void addParameter(SQLParameter parameter);

     String getAlias();
     String createTableAlias();
    String getQuoteName(String value);
//    void extractParameters(ExpressionContext sqlExpressionContext);
//    void clearParameters();
    void deleteThrow(boolean ifDeleteThrowException);
    boolean isDeleteThrow();
    EasyBehavior getBehavior();
    void useSQLStrategy(SQLExecuteStrategyEnum sqlStrategy);
    SQLExecuteStrategyEnum getSQLStrategy();
    void setVersion(Object version);
    Object getVersion();
    void useInterceptor(String name);
    void noInterceptor(String name);
    void useInterceptor();
    void noInterceptor();
    Stream<EasyInterceptorEntry> getInterceptorFilter(List<EasyInterceptorEntry> queryInterceptors);

   default void executeMethod(ExecuteMethodEnum executeMethod){
       executeMethod(executeMethod,false);
   }
    void executeMethod(ExecuteMethodEnum executeMethod,boolean ifUnknown);
    ExecuteMethodEnum getExecuteMethod();

    void setMaxShardingQueryLimit(int maxShardingQueryLimit);
    Integer getMaxShardingQueryLimitOrNull();
    void setConnectionMode(ConnectionModeEnum connectionMode);
    ConnectionModeEnum getConnectionModeOrNull();

    /**
     * 设置当前为sharding需要解析
     * 可以对非sharding的表达式进行优化不需要判断
     */
    void useSharding();
    boolean isSharding();
    void extract(ExpressionContext otherExpressionContext);
}
