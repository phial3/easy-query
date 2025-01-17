package com.easy.query.core.expression.lambda;

import java.io.Serializable;

/**
 *
 * @FileName: SFunction.java
 * @Description: 文件说明
 * @Date: 2023/2/4 22:16
 * @author xuejiaming
 */
@FunctionalInterface
public interface PropertySetter<T,R> extends Serializable {
    R apply(T t,Object value);
}

