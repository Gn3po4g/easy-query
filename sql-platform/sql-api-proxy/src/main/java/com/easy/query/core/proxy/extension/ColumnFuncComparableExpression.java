package com.easy.query.core.proxy.extension;

import com.easy.query.core.func.SQLFunc;
import com.easy.query.core.proxy.SQLOrderByExpression;
import com.easy.query.core.proxy.SQLSelectAsExpression;
import com.easy.query.core.proxy.TablePropColumn;
import com.easy.query.core.proxy.impl.SQLSelectAsImpl;
import com.easy.query.core.proxy.predicate.aggregate.DSLFunctionAggregatePredicate;
import com.easy.query.core.proxy.predicate.aggregate.DSLLikeAggregatePredicate;
import com.easy.query.core.proxy.predicate.aggregate.DSLOtherAggregatePredicate;
import com.easy.query.core.proxy.predicate.aggregate.DSLRangeAggregatePredicate;
import com.easy.query.core.proxy.predicate.aggregate.DSLSQLFunctionAssertPredicate;
import com.easy.query.core.proxy.predicate.aggregate.DSLSubQueryAggregatePredicate;
import com.easy.query.core.proxy.predicate.aggregate.DSLValueAggregatePredicate;
import com.easy.query.core.proxy.predicate.aggregate.DSLValuesAggregatePredicate;

/**
 * create time 2023/12/3 09:49
 * 文件说明
 *
 * @author xuejiaming
 */
public interface ColumnFuncComparableExpression<T> extends ColumnComparableExpression<T>, SQLOrderByExpression,
        DSLValueAggregatePredicate<T> ,
        DSLLikeAggregatePredicate<T>,
        DSLFunctionAggregatePredicate<T>,
        DSLOtherAggregatePredicate<T>,
        DSLSubQueryAggregatePredicate<T>,
        DSLValuesAggregatePredicate<T>,
        DSLRangeAggregatePredicate<T>,
        DSLSQLFunctionAssertPredicate<T> {
    @Override
    default SQLSelectAsExpression as(TablePropColumn propColumn) {
        return as(propColumn.getValue());
    }
    @Override
    default SQLSelectAsExpression as(String propertyAlias) {
        return new SQLSelectAsImpl(s -> {
            SQLFunc fx = s.getRuntimeContext().fx();
            s.columnFunc(this.getTable(), func().apply(fx), propertyAlias);
        }, s -> {
            SQLFunc fx = s.getRuntimeContext().fx();
            s.columnFunc(this.getTable(), this.getValue(), func().apply(fx), propertyAlias, () -> {
            });
        }, s -> {
            throw new UnsupportedOperationException();
        });
    }
}
