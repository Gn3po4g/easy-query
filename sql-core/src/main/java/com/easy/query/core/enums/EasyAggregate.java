package com.easy.query.core.enums;

import com.easy.query.core.basic.jdbc.executor.internal.merge.result.aggregation.AggregationType;

/**
 * @FileName: EasyAggregate.java
 * @Description: 文件说明
 * @Date: 2023/2/18 22:24
 * @author xuejiaming
 */
public enum EasyAggregate implements EasyFunc {
    SUM("SUM(%s)",AggregationType.SUM),
    COUNT("COUNT(%s)",AggregationType.COUNT),
    COUNT_DISTINCT("COUNT(DISTINCT %s)",AggregationType.COUNT_DISTINCT),
    MAX("MAX(%s)",AggregationType.MAX),
    MIN("MIN(%s)",AggregationType.MIN),
    AVG("AVG(%s)",AggregationType.AVG),
    LEN("LENGTH(%s)",AggregationType.LENGTH);
    private final String aggregate;
    private final AggregationType aggregationType;

    EasyAggregate(String aggregate, AggregationType aggregationType){

        this.aggregate = aggregate;
        this.aggregationType = aggregationType;
    }

    @Override
    public String getFuncColumn(String column) {
        return String.format(aggregate,column);
    }

    @Override
    public AggregationType getAggregationType() {
        return aggregationType;
    }
}
