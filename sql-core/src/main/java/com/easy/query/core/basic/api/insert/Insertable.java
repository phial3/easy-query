package com.easy.query.core.basic.api.insert;

import com.easy.query.core.basic.api.internal.Interceptable;
import com.easy.query.core.basic.api.internal.SQLExecuteRows;
import com.easy.query.core.basic.api.internal.SQLExecuteStrategy;
import com.easy.query.core.basic.api.internal.TableReNameable;

import java.util.Collection;

/**
 * @FileName: Insertable.java
 * @Description: 文件说明
 * @Date: 2023/2/20 08:48
 * @author xuejiaming
 */
public interface Insertable<T> extends SQLExecuteRows, Interceptable<Insertable<T>>, TableReNameable<Insertable<T>>, SQLExecuteStrategy<Insertable<T>> {
    Insertable<T> insert(T entity);

    default Insertable<T> insert(Collection<T> entities) {
        if(entities==null){
            return this;
        }
        for (T entity : entities) {
            insert(entity);
        }
        return this;
    }

    /**
     *
     * @param fillAutoIncrement
     * @return
     */
    long executeRows(boolean fillAutoIncrement);

    @Override
    default long executeRows(){
        return executeRows(false);
    }
}
