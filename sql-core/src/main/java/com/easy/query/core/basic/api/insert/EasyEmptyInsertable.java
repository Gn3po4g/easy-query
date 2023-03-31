package com.easy.query.core.basic.api.insert;

import com.easy.query.core.abstraction.EasySqlApiFactory;
import com.easy.query.core.expression.sql.EntityInsertExpression;

import java.util.function.Function;

/**
 * @FileName: EasyEmptyInsertable.java
 * @Description: 文件说明
 * @Date: 2023/3/6 08:49
 * @author xuejiaming
 */
public class EasyEmptyInsertable<T>implements Insertable<T> {
    private final EntityInsertExpression sqlEntityInsertExpression;

    public EasyEmptyInsertable(EntityInsertExpression sqlEntityInsertExpression){

        this.sqlEntityInsertExpression = sqlEntityInsertExpression;
    }
    @Override
    public Insertable<T> insert(T entity) {
        if(entity==null){
            return  this;
        }
        EasySqlApiFactory sqlApiFactory = sqlEntityInsertExpression.getRuntimeContext().getSqlApiFactory();
        Insertable<T> insertable = (Insertable<T>) sqlApiFactory.createInsertable((Class<T>) entity.getClass(), sqlEntityInsertExpression);
        insertable.insert(entity);
        return insertable;
    }

    @Override
    public Insertable<T> asTable(Function<String, String> tableNameAs) {
        return this;
    }

    @Override
    public long executeRows(boolean fillAutoIncrement) {
        return 0;
    }

    @Override
    public String toSql() {
        return null;
    }
}
