package org.easy.query.core.basic.api.delete;

import org.easy.query.core.abstraction.metadata.ColumnMetadata;
import org.easy.query.core.abstraction.metadata.EntityMetadata;
import org.easy.query.core.basic.api.context.DeleteContext;
import org.easy.query.core.expression.builder.SqlSegmentBuilder;
import org.easy.query.core.expression.segment.predicate.node.ColumnPropertyPredicate;
import org.easy.query.core.enums.MultiTableTypeEnum;
import org.easy.query.core.exception.EasyQueryException;
import org.easy.query.core.query.builder.SqlTableInfo;
import org.easy.query.core.util.ClassUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @FileName: AbstractEntityDelete.java
 * @Description: 文件说明
 * @Date: 2023/2/28 12:33
 * @Created by xuejiaming
 */
public abstract class AbstractEntityDelete<T> implements EasyDelete<T> {
    protected final List<T> entities= new ArrayList<>();
    protected final SqlTableInfo table;
    protected final DeleteContext deleteContext;

    public AbstractEntityDelete(Collection<T> entities, DeleteContext deleteContext){
        if(entities==null||entities.isEmpty()){
            throw new EasyQueryException("不支持空对象的delete");
        }
        this.entities.addAll(entities);
        this.deleteContext = deleteContext;

        Class<?> clazz = entities.iterator().next().getClass();
        EntityMetadata entityMetadata = deleteContext.getRuntimeContext().getEntityMetadataManager().getEntityMetadata(clazz);
        entityMetadata.checkTable();
        table=new SqlTableInfo(entityMetadata, null, 0, MultiTableTypeEnum.FROM);
        deleteContext.addSqlTable(table);
    }
    @Override
    public long executeRows() {

        if (!entities.isEmpty()) {
//            EasyQueryRuntimeContext runtimeContext = updateContext.getRuntimeContext();
//            EntityMetadataManager entityMetadataManager = runtimeContext.getEntityMetadataManager();

            String s = toSql();
            System.out.println("删除sql："+s);
        }
        return 0;
    }

    @Override
    public void executeRows(Long expectRow, String error) {

    }
}
