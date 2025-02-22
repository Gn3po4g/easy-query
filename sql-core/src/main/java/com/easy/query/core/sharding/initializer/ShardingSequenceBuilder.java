package com.easy.query.core.sharding.initializer;

import com.easy.query.core.enums.ExecuteMethodEnum;
import com.easy.query.core.enums.sharding.ConnectionModeEnum;
import com.easy.query.core.exception.EasyQueryInvalidOperationException;
import com.easy.query.core.metadata.EntityMetadata;
import com.easy.query.core.util.EasyBitwiseUtil;

/**
 * create time 2023/5/7 23:52
 * 文件说明
 *
 * @author xuejiaming
 */
public class ShardingSequenceBuilder<T> {
    private final ShardingEntityBuilder<T> baseBuilder;
    private final ShardingInitOptionBuilder shardingInitOptionBuilder;
    private final EntityMetadata entityMetadata;

    public ShardingSequenceBuilder(ShardingEntityBuilder<T> baseBuilder, ShardingInitOptionBuilder shardingInitOptionBuilder, EntityMetadata entityMetadata) {
        this.baseBuilder = baseBuilder;
        this.shardingInitOptionBuilder = shardingInitOptionBuilder;

        this.entityMetadata = entityMetadata;
    }

    /**
     * 当前属性asc的时候采用 defaultTableComparator的defaultTableNameComparator
     *
     * @param propertyName
     * @return
     */
    public ShardingSequenceBuilder<T> addPropertyDefaultUseAsc(String propertyName) {
        if (entityMetadata.getColumnOrNull(propertyName) == null) {
            throw new EasyQueryInvalidOperationException("sharding initializer add asc unknown property:" + propertyName);
        }
        shardingInitOptionBuilder.getSequenceProperties().put(propertyName, true);
        return this;
    }

    /**
     * 当前属性desc的时候采用 defaultTableComparator的defaultTableNameComparator
     *
     * @param propertyName
     * @return
     */
    public ShardingSequenceBuilder<T> addPropertyDefaultUseDesc(String propertyName) {
        if (entityMetadata.getColumnOrNull(propertyName) == null) {
            throw new EasyQueryInvalidOperationException("sharding initializer add desc unknown property:" + propertyName);
        }
        shardingInitOptionBuilder.getSequenceProperties().put(propertyName, false);
        return this;
    }

    /**
     * 表达式没有order的时候哪些方法需要支持顺序排序,比如我查询list当我不加orderBy的时候默认用{@link ShardingSequenceBuilder#addPropertyDefaultUseDesc}作为排序进行查询
     *
     * @param asc
     * @param executeMethods
     * @return
     */
    public ShardingSequenceBuilder<T> defaultAffectedMethod(boolean asc, ExecuteMethodEnum... executeMethods) {
        int sequenceCompareMethods = ExecuteMethodEnum.UNKNOWN.getCode();
        int sequenceCompareAscMethods = ExecuteMethodEnum.UNKNOWN.getCode();
        for (ExecuteMethodEnum executeMethod : executeMethods) {
            int executeMethodCode = executeMethod.getCode();
            sequenceCompareAscMethods = EasyBitwiseUtil.removeBit(sequenceCompareAscMethods, executeMethodCode);
            sequenceCompareMethods = EasyBitwiseUtil.addBit(sequenceCompareMethods, executeMethodCode);
            if (asc) {
                sequenceCompareAscMethods = EasyBitwiseUtil.addBit(sequenceCompareAscMethods, executeMethodCode);
            }
        }
        shardingInitOptionBuilder.setSequenceCompareMethods(sequenceCompareMethods);
        shardingInitOptionBuilder.setSequenceCompareAscMethods(sequenceCompareAscMethods);
        return this;
    }

    /**
     * 当符合顺序查询时使用的connectionsLimit
     *
     * @param maxShardingQueryLimit
     * @param executeMethods
     * @return
     */
    public ShardingSequenceBuilder<T> useMaxShardingQueryLimit(int maxShardingQueryLimit,ExecuteMethodEnum firstMethod, ExecuteMethodEnum... executeMethods) {
        shardingInitOptionBuilder.setMaxShardingQueryLimit(maxShardingQueryLimit);
        int sequenceLimitMethods = ExecuteMethodEnum.UNKNOWN.getCode();
        int firstMethodCode = firstMethod.getCode();
        sequenceLimitMethods = EasyBitwiseUtil.addBit(sequenceLimitMethods,firstMethodCode);
        for (ExecuteMethodEnum executeMethod : executeMethods) {
            int executeMethodCode = executeMethod.getCode();
            sequenceLimitMethods = EasyBitwiseUtil.addBit(sequenceLimitMethods, executeMethodCode);
        }
        shardingInitOptionBuilder.setSequenceLimitMethods(sequenceLimitMethods);
        return this;
    }

    public ShardingSequenceBuilder<T> useConnectionMode(ConnectionModeEnum connectionMode, ExecuteMethodEnum... executeMethods) {
        shardingInitOptionBuilder.setConnectionMode(connectionMode);
        int sequenceConnectionModeMethods = ExecuteMethodEnum.UNKNOWN.getCode();
        for (ExecuteMethodEnum executeMethod : executeMethods) {
            int executeMethodCode = executeMethod.getCode();
            sequenceConnectionModeMethods = EasyBitwiseUtil.addBit(sequenceConnectionModeMethods, executeMethodCode);
        }
        shardingInitOptionBuilder.setSequenceConnectionModeMethods(sequenceConnectionModeMethods);
        return this;
    }

    public ShardingEntityBuilder<T> and() {
        return baseBuilder;
    }


}
