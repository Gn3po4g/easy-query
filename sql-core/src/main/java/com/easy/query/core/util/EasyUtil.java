package com.easy.query.core.util;

import com.easy.query.core.common.bean.FastBean;
import com.easy.query.core.exception.EasyQueryException;
import com.easy.query.core.exception.EasyQueryInvalidOperationException;
import com.easy.query.core.expression.lambda.Property;
import com.easy.query.core.expression.lambda.PropertySetterCaller;
import com.easy.query.core.expression.segment.SqlEntityAliasSegment;
import com.easy.query.core.expression.sql.EntityDeleteExpression;
import com.easy.query.core.expression.sql.EntityExpression;
import com.easy.query.core.expression.sql.EntityInsertExpression;
import com.easy.query.core.expression.sql.EntityQueryExpression;
import com.easy.query.core.expression.sql.EntityTableExpression;
import com.easy.query.core.expression.sql.EntityUpdateExpression;
import com.easy.query.core.metadata.ColumnMetadata;
import com.easy.query.core.sharding.merge.executor.common.Grouping;
import com.easy.query.core.sharding.merge.executor.common.GroupingImpl;
import com.easy.query.core.sharding.merge.executor.internal.CommandTypeEnum;

import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author xuejiaming
 * @FileName: EasyUtil.java
 * @Description: 文件说明
 * @Date: 2023/3/4 13:12
 */
public class EasyUtil {


    private static final int FLAG_SERIALIZABLE = 1;

    private EasyUtil() {
    }

    public static <T, K> Stream<Grouping<K, T>> groupBy(Stream<T> stream, Function<T, K> keyExtractor) {
        Map<K, List<T>> map = stream.collect(Collectors.groupingBy(keyExtractor));
        return map.entrySet().stream().map(e -> new GroupingImpl<>(e.getKey(), e.getValue().stream()));
    }
    public static <T, K> Map<K, List<T>> groupByToMap(Stream<T> stream, Function<T, K> keyExtractor) {
        return stream.collect(Collectors.groupingBy(keyExtractor));
    }
//    public static <T, K,V> List<V> groupBy(List<T> list, Function<T, K> keyExtractor,Function<Grouping<K,T>,V> selector) {
//        Map<K, List<T>> map = list.stream().collect(Collectors.groupingBy(keyExtractor));
//        return map.entrySet().stream().map(e -> selector.apply(new Grouping<>(e.getKey(), e.getValue()))).collect(Collectors.toList());
//    }
    public static EntityTableExpression getPredicateTableByOffset(EntityQueryExpression sqlEntityExpression, int offsetForward) {
        List<EntityTableExpression> tables = sqlEntityExpression.getTables();
        if (tables.isEmpty()) {
            throw new EasyQueryException("cant get current join table");
        }
        int i = getNextTableIndex(sqlEntityExpression) - 1 - offsetForward;
        return tables.get(i);
    }

    public static EntityTableExpression getCurrentPredicateTable(EntityQueryExpression sqlEntityExpression) {
        return getPredicateTableByOffset(sqlEntityExpression, 0);
    }

    public static EntityTableExpression getPreviewPredicateTable(EntityQueryExpression sqlEntityExpression) {
        return getPredicateTableByOffset(sqlEntityExpression, 1);
    }

    public static ColumnMetadata getColumnMetadata(EntityTableExpression tableExpression, String propertyName) {
        return tableExpression.getEntityMetadata().getColumnNotNull(propertyName);
    }

    public static int getNextTableIndex(EntityQueryExpression sqlEntityExpression) {
        return sqlEntityExpression.getTables().size();
    }

    public static String getAnonymousPropertyName(SqlEntityAliasSegment sqlEntityProject) {
        String alias = sqlEntityProject.getAlias();
        if (StringUtil.isBlank(alias)) {
            return sqlEntityProject.getPropertyName();
        }
        return sqlEntityProject.getTable().getEntityMetadata().getPropertyNameOrDefault(alias,alias);
    }

    private static Map<Class<?>, FastBean> CLASS_PROPERTY_FAST_BEAN_CACHE = new ConcurrentHashMap<>();

    public static FastBean getFastBean(Class<?> entityClass) {
        return CLASS_PROPERTY_FAST_BEAN_CACHE.computeIfAbsent(entityClass, key -> new FastBean(entityClass));
    }
    public static Property<Object, ?> getPropertyGetterLambda(Class<?> entityClass, String propertyName, Class<?> fieldType) {
        return getFastBean(entityClass).getBeanGetter(propertyName, fieldType);
    }
    public static PropertySetterCaller<Object> getPropertySetterLambda(Class<?> entityClass, PropertyDescriptor prop) {
        return getFastBean(entityClass).getBeanSetter(prop);
    }

    public static CommandTypeEnum getCommandType(EntityExpression entityExpression){
        if(entityExpression instanceof  EntityQueryExpression){
            return CommandTypeEnum.QUERY;
        }
        if(entityExpression instanceof EntityInsertExpression){
            return CommandTypeEnum.EXECUTE_BATCH;
        }
        if(entityExpression instanceof EntityUpdateExpression){
            EntityUpdateExpression entityUpdateExpression = (EntityUpdateExpression) entityExpression;
            return entityUpdateExpression.isExpression()?CommandTypeEnum.EXECUTE: CommandTypeEnum.EXECUTE_BATCH;
        }
        if(entityExpression instanceof EntityDeleteExpression){
            EntityDeleteExpression entityDeleteExpression = (EntityDeleteExpression) entityExpression;
            return entityDeleteExpression.isExpression()?CommandTypeEnum.EXECUTE: CommandTypeEnum.EXECUTE_BATCH;
        }
        throw new EasyQueryInvalidOperationException(ClassUtil.getInstanceSimpleName(entityExpression)+" cant get commandType");
    }
}

