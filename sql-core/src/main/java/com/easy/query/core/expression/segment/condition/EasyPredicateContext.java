package com.easy.query.core.expression.segment.condition;

import com.easy.query.core.expression.segment.condition.predicate.Predicate;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @FileName: EasyPredicateContext.java
 * @Description: 文件说明
 * @Date: 2023/3/15 09:39
 * @author xuejiaming
 */
public class EasyPredicateContext implements PredicateContext,PredicateIndex {
    private final Map<Class<?>, Set<String>> entityProperties=new LinkedHashMap<>();
    public void add(Predicate predicate){
        Set<String> propertySet = entityProperties.computeIfAbsent(predicate.getTable().getEntityClass(), key -> new HashSet<>());
        propertySet.add(predicate.getPropertyName());
    }
    public boolean contains(Class<?> entityClass, String propertyName){
        Set<String> propertySet = entityProperties.get(entityClass);
        if (propertySet != null) {
            return propertySet.contains(propertyName);
        }
        return false;
    }
}
