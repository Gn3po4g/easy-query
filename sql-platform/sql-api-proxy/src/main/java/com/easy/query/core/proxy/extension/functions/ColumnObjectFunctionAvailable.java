package com.easy.query.core.proxy.extension.functions;

import com.easy.query.core.expression.lambda.SQLExpression1;
import com.easy.query.core.expression.parser.core.available.TableAvailable;
import com.easy.query.core.func.SQLFunc;
import com.easy.query.core.func.SQLFunction;
import com.easy.query.core.proxy.PropTypeColumn;
import com.easy.query.core.proxy.SQLSelectAsExpression;
import com.easy.query.core.proxy.core.EntitySQLContext;
import com.easy.query.core.proxy.extension.functions.executor.ColumnFunctionCompareComparableBooleanChainExpression;
import com.easy.query.core.proxy.extension.functions.executor.ColumnFunctionCompareComparableNumberChainExpression;
import com.easy.query.core.proxy.extension.functions.executor.ColumnFunctionCompareComparableNumberFilterChainExpression;
import com.easy.query.core.proxy.extension.functions.executor.impl.ColumnFunctionCompareComparableBooleanChainExpressionImpl;
import com.easy.query.core.proxy.extension.functions.executor.impl.ColumnFunctionCompareComparableNumberChainExpressionImpl;
import com.easy.query.core.proxy.extension.functions.executor.impl.ColumnFunctionCompareComparableNumberFilterChainExpressionImpl;
import com.easy.query.core.proxy.func.column.ProxyColumnFuncSelector;
import com.easy.query.core.proxy.func.column.ProxyColumnFuncSelectorImpl;
import com.easy.query.core.proxy.predicate.aggregate.DSLSQLFunctionAvailable;

import java.util.function.Function;

/**
 * create time 2023/12/24 00:10
 * 文件说明
 *
 * @author xuejiaming
 */
public interface ColumnObjectFunctionAvailable<TProperty, TChain> extends SQLSelectAsExpression, PropTypeColumn<TProperty> {
    TChain createChainExpression(EntitySQLContext entitySQLContext, TableAvailable table, String property, Function<SQLFunc, SQLFunction> func, Class<?> propType);

    default <T extends Long> ColumnFunctionCompareComparableNumberFilterChainExpression<T> count() {
        return count(false);
    }

//    default <T extends Long> ColumnFunctionCompareComparableNumberChainExpression<T> count(boolean distinct) {
//        return new ColumnFunctionCompareComparableNumberChainExpressionImpl<>(this.getEntitySQLContext(), this.getTable(), this.getValue(), fx -> {
//            if (this instanceof DSLSQLFunctionAvailable) {
//                SQLFunction sqlFunction = ((DSLSQLFunctionAvailable) this).func().apply(fx);
//                return fx.count(sqlFunction).distinct(distinct);
//            } else {
//                return fx.count(this.getValue()).distinct(distinct);
//            }
//        }, Long.class);
//    }

    default <T extends Long> ColumnFunctionCompareComparableNumberFilterChainExpression<T> count(boolean distinct) {
        return new ColumnFunctionCompareComparableNumberFilterChainExpressionImpl<>(this.getEntitySQLContext(),this, this.getTable(), this.getValue(), (self,fx) -> {
            if (self instanceof DSLSQLFunctionAvailable) {
                SQLFunction sqlFunction = ((DSLSQLFunctionAvailable) self).func().apply(fx);
                return fx.count(sqlFunction).distinct(distinct);
            } else {
                return fx.count(self.getValue()).distinct(distinct);
            }
        }, Long.class);
    }
    default <T extends Integer> ColumnFunctionCompareComparableNumberFilterChainExpression<T> intCount() {
        return intCount(false);
    }

    default <T extends Integer> ColumnFunctionCompareComparableNumberFilterChainExpression<T> intCount(boolean distinct) {
        return new ColumnFunctionCompareComparableNumberFilterChainExpressionImpl<>(this.getEntitySQLContext(), this,this.getTable(), this.getValue(), (self,fx) -> {
            if (self instanceof DSLSQLFunctionAvailable) {
                SQLFunction sqlFunction = ((DSLSQLFunctionAvailable) self).func().apply(fx);
                return fx.count(sqlFunction).distinct(distinct);
            } else {
                return fx.count(self.getValue()).distinct(distinct);
            }
        }, Integer.class);
    }

    default TChain max() {
        return createChainExpression(this.getEntitySQLContext(), this.getTable(), this.getValue(), fx -> {
            if (this instanceof DSLSQLFunctionAvailable) {
                SQLFunction sqlFunction = ((DSLSQLFunctionAvailable) this).func().apply(fx);
                return fx.max(sqlFunction);
            } else {
                return fx.max(this.getValue());
            }
        }, getPropertyType());
    }

    default TChain min() {
        return createChainExpression(this.getEntitySQLContext(), this.getTable(), this.getValue(), fx -> {
            if (this instanceof DSLSQLFunctionAvailable) {
                SQLFunction sqlFunction = ((DSLSQLFunctionAvailable) this).func().apply(fx);
                return fx.min(sqlFunction);
            } else {
                return fx.min(this.getValue());
            }
        }, getPropertyType());
    }

    default TChain nullOrDefault(TProperty value) {
        return nullOrDefault(o -> o.value(_toFunctionSerializeValue(value)));
    }
    default TChain nullOrDefault(PropTypeColumn<TProperty> propTypeColumn) {
        return nullOrDefault(x->{
            PropTypeColumn.columnFuncSelector(x.getColumnFuncSelector(),propTypeColumn);
        });
    }

    default TChain nullOrDefault(SQLExpression1<ProxyColumnFuncSelector> selector) {
        return createChainExpression(this.getEntitySQLContext(), this.getTable(), this.getValue(), fx -> {
            return fx.nullOrDefault(o -> {
                PropTypeColumn.columnFuncSelector(o,this);
                selector.apply(new ProxyColumnFuncSelectorImpl(o));
            });
        }, getPropertyType());
    }


    default ColumnFunctionCompareComparableBooleanChainExpression<Boolean> equalsWith(TProperty value){
        return equalsWith(x->x.value(_toFunctionSerializeValue(value)));
    }
    default ColumnFunctionCompareComparableBooleanChainExpression<Boolean> equalsWith(PropTypeColumn<TProperty> propTypeColumn){
        return equalsWith(x->{
            PropTypeColumn.columnFuncSelector(x.getColumnFuncSelector(),propTypeColumn);
        });
    }
    default ColumnFunctionCompareComparableBooleanChainExpression<Boolean> equalsWith(SQLExpression1<ProxyColumnFuncSelector> selector){
        return new ColumnFunctionCompareComparableBooleanChainExpressionImpl<>(this.getEntitySQLContext(), this.getTable(), this.getValue(), fx -> {
            return fx.equalsWith(o -> {
                PropTypeColumn.columnFuncSelector(o,this);
                selector.apply(new ProxyColumnFuncSelectorImpl(o));
            });
        }, Boolean.class);
    }

}
