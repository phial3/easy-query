package com.easy.query.core.expression.parser.core;

import com.easy.query.core.basic.api.select.Queryable;
import com.easy.query.core.expression.func.ColumnFunction;
import com.easy.query.core.enums.SQLLikeEnum;
import com.easy.query.core.enums.SQLPredicateCompare;
import com.easy.query.core.enums.SQLRangeEnum;
import com.easy.query.core.expression.lambda.Property;
import com.easy.query.core.expression.lambda.SQLExpression1;
import com.easy.query.core.expression.parser.core.available.TableAvailable;

import java.util.Collection;

/**
 * @FileName: WherePredicate.java
 * @Description: 文件说明
 * @Date: 2023/2/5 09:09
 * @author xuejiaming
 */
public interface SQLWherePredicate<T1> {
    TableAvailable getTable();
    /**
     * 大于 column > val
     */
    default SQLWherePredicate<T1> gt(Property<T1, ?> column, Object val) {
        return gt(true, column, val);
    }

    /**
     * 大于 column > val
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    SQLWherePredicate<T1> gt(boolean condition, Property<T1, ?> column, Object val);

    /**
     * 等于 column >= val
     */
    default SQLWherePredicate<T1> ge(Property<T1, ?> column, Object val) {
        return ge(true, column, val);
    }

    /**
     * 等于 column >= val
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    SQLWherePredicate<T1> ge(boolean condition, Property<T1, ?> column, Object val);

    /**
     * 等于 column = val
     */
    default SQLWherePredicate<T1> eq(Property<T1, ?> column, Object val) {
        return eq(true, column, val);
    }

    /**
     * 等于 column = val
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    SQLWherePredicate<T1> eq(boolean condition, Property<T1, ?> column, Object val);

    /**
     * 不等于 column <> val
     */
    default SQLWherePredicate<T1> ne(Property<T1, ?> column, Object val) {
        return ne(true, column, val);
    }

    /**
     * 不等于 column <> val
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    SQLWherePredicate<T1> ne(boolean condition, Property<T1, ?> column, Object val);

    /**
     * 小于等于 column <= val
     */
    default SQLWherePredicate<T1> le(Property<T1, ?> column, Object val) {
        return le(true, column, val);
    }

    /**
     * 小于等于 column <= val
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    SQLWherePredicate<T1> le(boolean condition, Property<T1, ?> column, Object val);

    /**
     * 小于 column < val
     */
    default SQLWherePredicate<T1> lt(Property<T1, ?> column, Object val) {
        return lt(true, column, val);
    }

    /**
     * 小于 column < val
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    SQLWherePredicate<T1> lt(boolean condition, Property<T1, ?> column, Object val);

    /**
     * column like val%
     * 列匹配前半部分
     * @param column
     * @param val
     * @return
     */
    default SQLWherePredicate<T1> likeMatchLeft(Property<T1, ?> column, Object val) {
        return likeMatchLeft(true, column, val);
    }

    /**
     * column like val%
     * 列匹配前半部分
     * @param condition
     * @param column
     * @param val
     * @return
     */
    default SQLWherePredicate<T1> likeMatchLeft(boolean condition, Property<T1, ?> column, Object val) {
        return like(condition, column, val, SQLLikeEnum.LIKE_START);
    }

    /**
     * column like %val
     * 列匹配后半部分
     * @param column
     * @param val
     * @return
     */
    default SQLWherePredicate<T1> likeMatchRight(Property<T1, ?> column, Object val) {
        return likeMatchRight(true, column, val);
    }

    /**
     * column like %val
     * 列匹配后半部分
     * @param condition
     * @param column
     * @param val
     * @return
     */
    default SQLWherePredicate<T1> likeMatchRight(boolean condition, Property<T1, ?> column, Object val) {
        return like(condition, column, val, SQLLikeEnum.LIKE_END);
    }

    /**
     * column like %val%
     * 列全匹配
     */
    default SQLWherePredicate<T1> like(Property<T1, ?> column, Object val) {
        return like(true, column, val);
    }

    /**
     * column like %val%
     * 列全匹配
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    default SQLWherePredicate<T1> like(boolean condition, Property<T1, ?> column, Object val) {
        return like(condition, column, val, SQLLikeEnum.LIKE_ALL);
    }

    /**
     * column like ?val?
     * 列自定义匹配
     * @param condition
     * @param column
     * @param val
     * @param sqlLike
     * @return
     */
    SQLWherePredicate<T1> like(boolean condition, Property<T1, ?> column, Object val, SQLLikeEnum sqlLike);

    /**
     * column not like val%
     * @param column
     * @param val
     * @return
     */
    default SQLWherePredicate<T1> notLikeMatchLeft(Property<T1, ?> column, Object val) {
        return notLikeMatchLeft(true, column, val);
    }

    /**
     * column not like val%
     * @param condition
     * @param column
     * @param val
     * @return
     */
    default SQLWherePredicate<T1> notLikeMatchLeft(boolean condition, Property<T1, ?> column, Object val) {
        return notLike(condition, column, val, SQLLikeEnum.LIKE_ALL);
    }

    /**
     * column not like %val
     * @param column
     * @param val
     * @return
     */
    default SQLWherePredicate<T1> notLikeMatchRight(Property<T1, ?> column, Object val) {
        return notLikeMatchRight(true, column, val);
    }

    /**
     * column not like %val
     * @param condition
     * @param column
     * @param val
     * @return
     */
    default SQLWherePredicate<T1> notLikeMatchRight(boolean condition, Property<T1, ?> column, Object val) {
        return notLike(condition, column, val, SQLLikeEnum.LIKE_END);
    }

    /**
     * column not like %val%
     */
    default SQLWherePredicate<T1> notLike(Property<T1, ?> column, Object val) {
        return notLike(true, column, val);
    }

    /**
     * column not like %val%
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    default SQLWherePredicate<T1> notLike(boolean condition, Property<T1, ?> column, Object val) {
        return notLike(condition, column, val, SQLLikeEnum.LIKE_ALL);
    }

    SQLWherePredicate<T1> notLike(boolean condition, Property<T1, ?> column, Object val, SQLLikeEnum sqlLike);

    /**
     * column is null
     */
    default SQLWherePredicate<T1> isNull(Property<T1, ?> column) {
        return isNull(true, column);
    }

    /**
     * column is null
     *
     * @param condition 执行条件
     * @param column    字段
     * @return children
     */
    SQLWherePredicate<T1> isNull(boolean condition, Property<T1, ?> column);

    /**
     * column is not null
     */
    default SQLWherePredicate<T1> isNotNull(Property<T1, ?> column) {
        return isNotNull(true, column);
    }

    /**
     * column is not null
     *
     * @param condition 执行条件
     * @param column    字段
     * @return children
     */
    SQLWherePredicate<T1> isNotNull(boolean condition, Property<T1, ?> column);

    /**
     * column in collection
     * 集合为空返回False
     */
    default SQLWherePredicate<T1> in(Property<T1, ?> column, Collection<?> collection) {
        return in(true, column, collection);
    }

    /**
     * column in collection
     * 集合为空返回False
     */
    SQLWherePredicate<T1> in(boolean condition, Property<T1, ?> column, Collection<?> collection);
   default  <TProperty> SQLWherePredicate<T1> in(Property<T1, TProperty> column, Queryable<TProperty> subQueryable){
       return in(true,column,subQueryable);
   }
    <TProperty> SQLWherePredicate<T1> in(boolean condition, Property<T1, TProperty> column, Queryable<TProperty> subQueryable);

    /**
     * column not in collection
     * 集合为空返回True
     */
    default SQLWherePredicate<T1> notIn(Property<T1, ?> column, Collection<?> collection) {
        return notIn(true, column, collection);
    }

    /**
     * column not in collection
     * 集合为空返回True
     */
    SQLWherePredicate<T1> notIn(boolean condition, Property<T1, ?> column, Collection<?> collection);
    default  <TProperty> SQLWherePredicate<T1> notIn(Property<T1, ?> column, Queryable<TProperty> subQueryable){
        return notIn(true,column,subQueryable);
    }
    <TProperty> SQLWherePredicate<T1> notIn(boolean condition, Property<T1, ?> column, Queryable<TProperty> subQueryable);

   default  <T2> SQLWherePredicate<T1> exists(Queryable<T2> subQueryable, SQLExpression1<SQLWherePredicate<T2>> whereExpression){
       return exists(true,subQueryable,whereExpression);
   }
    <T2> SQLWherePredicate<T1> exists(boolean condition, Queryable<T2> subQueryable, SQLExpression1<SQLWherePredicate<T2>> whereExpression);

   default  <T2> SQLWherePredicate<T1> notExists(Queryable<T2> subQueryable, SQLExpression1<SQLWherePredicate<T2>> whereExpression){
       return notExists(true,subQueryable,whereExpression);
   }
    <T2> SQLWherePredicate<T1> notExists(boolean condition, Queryable<T2> subQueryable, SQLExpression1<SQLWherePredicate<T2>> whereExpression);

    /**
     * 区间 (left..right] = {x | left < x <= right}
     * 一般用于范围比如时间,小的时间在前大的时间在后
     * @param column
     * @param conditionLeft
     * @param valLeft
     * @param conditionRight
     * @param valRight
     * @return
     */
    default SQLWherePredicate<T1> rangeOpenClosed(Property<T1, ?> column, boolean conditionLeft, Object valLeft, boolean conditionRight, Object valRight) {
        return rangeOpenClosed(true, column, conditionLeft, valLeft, conditionRight, valRight);
    }

    /**
     * 区间 (left..right] = {x | left < x <= right}
     * 一般用于范围比如时间,小的时间在前大的时间在后
     * @param condition
     * @param column
     * @param conditionLeft
     * @param valLeft
     * @param conditionRight
     * @param valRight
     * @return
     */
    default SQLWherePredicate<T1> rangeOpenClosed(boolean condition, Property<T1, ?> column, boolean conditionLeft, Object valLeft, boolean conditionRight, Object valRight) {
        return range(condition, column, conditionLeft, valLeft, conditionRight, valRight, SQLRangeEnum.openClosed);
    }

    /**
     * 区间 (left..right) = {x | left < x < right}
     * 一般用于范围比如时间,小的时间在前大的时间在后
     * @param column 数据库列
     * @param valLeft 区间左侧值
     * @param valRight 区间右侧的值
     * @return
     */
    default SQLWherePredicate<T1> rangeOpen(Property<T1, ?> column, Object valLeft, Object valRight) {
        return rangeOpen(true, column, true, valLeft, true, valRight);
    }

    /**
     * 区间 (left..right) = {x | left < x < right}
     * 一般用于范围比如时间,小的时间在前大的时间在后
     * @param column 数据库列
     * @param conditionLeft 是否添加左侧条件
     * @param valLeft 区间左侧值
     * @param conditionRight 是否添加右侧条件
     * @param valRight 区间右侧的值
     * @return
     */
    default SQLWherePredicate<T1> rangeOpen(Property<T1, ?> column, boolean conditionLeft, Object valLeft, boolean conditionRight, Object valRight) {
        return rangeOpen(true, column, conditionLeft, valLeft, conditionRight, valRight);
    }

    /**
     * 区间 (left..right) = {x | left < x < right}
     * 一般用于范围比如时间,小的时间在前大的时间在后
     * @param condition
     * @param column
     * @param conditionLeft
     * @param valLeft
     * @param conditionRight
     * @param valRight
     * @return
     */
    default SQLWherePredicate<T1> rangeOpen(boolean condition, Property<T1, ?> column, boolean conditionLeft, Object valLeft, boolean conditionRight, Object valRight) {
        return range(condition, column, conditionLeft, valLeft, conditionRight, valRight, SQLRangeEnum.Open);
    }
    /**
     * [left..right) = {x | left <= x < right}
     * 一般用于范围比如时间,小的时间在前大的时间在后
     * @param column
     * @param conditionLeft
     * @param valLeft
     * @param conditionRight
     * @param valRight
     * @return
     */
    default SQLWherePredicate<T1> rangeClosedOpen(Property<T1, ?> column, boolean conditionLeft, Object valLeft, boolean conditionRight, Object valRight) {
        return rangeClosedOpen(true, column, conditionLeft, valLeft, conditionRight, valRight);
    }
    /**
     * [left..right) = {x | left <= x < right}
     * 一般用于范围比如时间,小的时间在前大的时间在后
     * @param condition
     * @param column
     * @param conditionLeft
     * @param valLeft
     * @param conditionRight
     * @param valRight
     * @return
     */
    default SQLWherePredicate<T1> rangeClosedOpen(boolean condition, Property<T1, ?> column, boolean conditionLeft, Object valLeft, boolean conditionRight, Object valRight) {
        return range(condition, column, conditionLeft, valLeft, conditionRight, valRight, SQLRangeEnum.closedOpen);
    }

    /**
     * [left..right] = {x | left <= x <= right}
     * 一般用于范围比如时间,小的时间在前大的时间在后
     * @param column
     * @param valLeft
     * @param valRight
     * @return
     */
    default SQLWherePredicate<T1> rangeClosed(Property<T1, ?> column, Object valLeft, Object valRight) {
        return rangeClosed(true, column, true, valLeft, true, valRight);
    }
    /**
     * [left..right] = {x | left <= x <= right}
     * 一般用于范围比如时间,小的时间在前大的时间在后
     * @param column
     * @param conditionLeft
     * @param valLeft
     * @param conditionRight
     * @param valRight
     * @return
     */
    default SQLWherePredicate<T1> rangeClosed(Property<T1, ?> column, boolean conditionLeft, Object valLeft, boolean conditionRight, Object valRight) {
        return rangeClosed(true, column, conditionLeft, valLeft, conditionRight, valRight);
    }

    /**
     * [left..right] = {x | left <= x <= right}
     * 一般用于范围比如时间,小的时间在前大的时间在后
     * @param condition
     * @param column
     * @param conditionLeft
     * @param valLeft
     * @param conditionRight
     * @param valRight
     * @return
     */
    default SQLWherePredicate<T1> rangeClosed(boolean condition, Property<T1, ?> column, boolean conditionLeft, Object valLeft, boolean conditionRight, Object valRight) {
        return range(condition, column, conditionLeft, valLeft, conditionRight, valRight, SQLRangeEnum.Closed);
    }

    /**
     * 自定义范围
     * 一般用于范围比如时间,小的时间在前大的时间在后
     * @param condition
     * @param column
     * @param conditionLeft
     * @param valLeft
     * @param conditionRight
     * @param valRight
     * @param sqlRange
     * @return
     */
    SQLWherePredicate<T1> range(boolean condition, Property<T1, ?> column, boolean conditionLeft, Object valLeft, boolean conditionRight, Object valRight, SQLRangeEnum sqlRange);


  default SQLWherePredicate<T1> columnFunc(Property<T1, ?> column, ColumnFunction easyFunc, SQLPredicateCompare sqlPredicateCompare, Object val){
      return columnFunc(true,column,easyFunc,sqlPredicateCompare,val);
  }
    SQLWherePredicate<T1> columnFunc(boolean condition, Property<T1, ?> column, ColumnFunction easyFunc, SQLPredicateCompare sqlPredicateCompare, Object val);
    default <T2, TChain2> SQLWherePredicate<T1> eq(SQLWherePredicate<T2> sub, Property<T1, ?> column1, Property<T2, ?> column2) {
        return eq(true, sub, column1, column2);
    }

    <T2> SQLWherePredicate<T1> eq(boolean condition, SQLWherePredicate<T2> sub, Property<T1, ?> column1, Property<T2, ?> column2);

    <T2> SQLWherePredicate<T2> then(SQLWherePredicate<T2> sub);

    default SQLWherePredicate<T1> and() {
        return and(true);
    }

    SQLWherePredicate<T1> and(boolean condition);

    default SQLWherePredicate<T1> and(SQLExpression1<SQLWherePredicate<T1>> sqlWherePredicateSQLExpression) {
        return and(true, sqlWherePredicateSQLExpression);
    }

    SQLWherePredicate<T1> and(boolean condition, SQLExpression1<SQLWherePredicate<T1>> sqlWherePredicateSQLExpression);

    default SQLWherePredicate<T1> or() {
        return or(true);
    }

    SQLWherePredicate<T1> or(boolean condition);

    default SQLWherePredicate<T1> or(SQLExpression1<SQLWherePredicate<T1>> sqlWherePredicateSQLExpression) {
        return or(true, sqlWherePredicateSQLExpression);
    }

    SQLWherePredicate<T1> or(boolean condition, SQLExpression1<SQLWherePredicate<T1>> sqlWherePredicateSQLExpression);
}
