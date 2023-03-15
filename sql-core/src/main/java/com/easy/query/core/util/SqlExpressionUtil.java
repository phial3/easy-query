package com.easy.query.core.util;

import com.easy.query.core.basic.api.select.Queryable;
import com.easy.query.core.basic.api.select.Queryable2;
import com.easy.query.core.basic.api.select.Queryable3;
import com.easy.query.core.expression.lambda.SqlExpression2;
import com.easy.query.core.expression.lambda.SqlExpression3;
import com.easy.query.core.expression.parser.abstraction.SqlPredicate;
import com.easy.query.core.expression.parser.abstraction.internal.ColumnSelector;
import com.easy.query.core.query.AnonymousEntityTableExpression;
import com.easy.query.core.query.SqlEntityQueryExpression;
import com.easy.query.core.basic.api.select.Queryable4;
import com.easy.query.core.expression.lambda.SqlExpression4;


/**
 * @FileName: SqlExpressionUtil.java
 * @Description: 文件说明
 * @Date: 2023/3/7 12:32
 * @Created by xuejiaming
 */
public class SqlExpressionUtil {
    private SqlExpressionUtil() {
    }

    public static <TSource> Queryable<TSource> cloneAndSelectAllQueryable(Queryable<TSource> queryable) {
        SqlEntityQueryExpression queryableSqlEntityExpression = queryable.getSqlEntityExpression();
        if (SqlExpressionUtil.shouldCloneSqlEntityQueryExpression(queryableSqlEntityExpression)) {
            return queryable.cloneQueryable().select(ColumnSelector::columnAll);
        }
        return queryable;
    }

    /**
     * 判断当前表达式是否需要对其进行select all 如果需要select all那么就表示需要克隆一个
     *
     * @param sqlEntityExpression
     * @return
     */
    public static boolean shouldCloneSqlEntityQueryExpression(SqlEntityQueryExpression sqlEntityExpression) {
        return sqlEntityExpression.getProjects().isEmpty() && !sqlEntityExpression.hasGroup() && moreTableExpressionOrNoAnonymous(sqlEntityExpression);
    }

    private static boolean moreTableExpressionOrNoAnonymous(SqlEntityQueryExpression sqlEntityExpression) {
        return sqlEntityExpression.getTables().size() != 1 || !(sqlEntityExpression.getTables().get(0) instanceof AnonymousEntityTableExpression) || sqlEntityExpression.hasLimit() || sqlEntityExpression.hasWhere() || sqlEntityExpression.hasOrder() || sqlEntityExpression.hasHaving();
    }

    public static <T1, T2> Queryable2<T1, T2> executeJoinOn(Queryable2<T1, T2> queryable, SqlExpression2<SqlPredicate<T1>, SqlPredicate<T2>> on) {
        SqlPredicate<T1> sqlOnPredicate1 = queryable.getSqlBuilderProvider2().getSqlOnPredicate1();
        SqlPredicate<T2> sqlOnPredicate2 = queryable.getSqlBuilderProvider2().getSqlOnPredicate2();
        on.apply(sqlOnPredicate1, sqlOnPredicate2);
        return queryable;
    }

    public static <T1, T2, T3> Queryable3<T1, T2, T3> executeJoinOn(Queryable3<T1, T2, T3> queryable, SqlExpression3<SqlPredicate<T1>, SqlPredicate<T2>, SqlPredicate<T3>> on) {
        SqlPredicate<T1> sqlOnPredicate1 = queryable.getSqlBuilderProvider3().getSqlOnPredicate1();
        SqlPredicate<T2> sqlOnPredicate2 = queryable.getSqlBuilderProvider3().getSqlOnPredicate2();
        SqlPredicate<T3> sqlOnPredicate3 = queryable.getSqlBuilderProvider3().getSqlOnPredicate3();
        on.apply(sqlOnPredicate1, sqlOnPredicate2, sqlOnPredicate3);
        return queryable;
    }
    public static <T1, T2, T3,T4> Queryable4<T1, T2, T3,T4> executeJoinOn(Queryable4<T1, T2, T3,T4> queryable, SqlExpression4<SqlPredicate<T1>, SqlPredicate<T2>, SqlPredicate<T3>, SqlPredicate<T4>> on) {
        SqlPredicate<T1> sqlOnPredicate1 = queryable.getSqlBuilderProvider4().getSqlOnPredicate1();
        SqlPredicate<T2> sqlOnPredicate2 = queryable.getSqlBuilderProvider4().getSqlOnPredicate2();
        SqlPredicate<T3> sqlOnPredicate3 = queryable.getSqlBuilderProvider4().getSqlOnPredicate3();
        SqlPredicate<T4> sqlOnPredicate4 = queryable.getSqlBuilderProvider4().getSqlOnPredicate4();
        on.apply(sqlOnPredicate1, sqlOnPredicate2, sqlOnPredicate3,sqlOnPredicate4);
        return queryable;
    }
}