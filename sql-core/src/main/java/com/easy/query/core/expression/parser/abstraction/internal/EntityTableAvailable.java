package com.easy.query.core.expression.parser.abstraction.internal;

import com.easy.query.core.metadata.EntityMetadata;

/**
 * create time 2023/5/1 21:42
 * 文件说明
 *
 * @author xuejiaming
 */
public interface EntityTableAvailable extends IndexAvailable{
    Class<?> getEntityClass();
    EntityMetadata getEntityMetadata();
    String getTableName();
    String getAlias();
    String getColumnName(String propertyName);
}
