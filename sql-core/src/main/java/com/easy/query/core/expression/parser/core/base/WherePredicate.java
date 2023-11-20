package com.easy.query.core.expression.parser.core.base;

import com.easy.query.core.context.QueryRuntimeContext;
import com.easy.query.core.enums.SQLPredicateCompare;
import com.easy.query.core.expression.func.ColumnPropertyFunction;
import com.easy.query.core.expression.lambda.SQLExpression1;
import com.easy.query.core.expression.lambda.SQLExpression2;
import com.easy.query.core.expression.parser.core.EntitySQLTableOwner;
import com.easy.query.core.expression.parser.core.available.SQLFxAvailable;
import com.easy.query.core.expression.parser.core.base.core.SQLPropertyNative;
import com.easy.query.core.expression.parser.core.base.core.filter.AssertPredicate;
import com.easy.query.core.expression.parser.core.base.core.filter.FuncColumnPredicate;
import com.easy.query.core.expression.parser.core.base.core.filter.FuncValuePredicate;
import com.easy.query.core.expression.parser.core.base.core.filter.LikePredicate;
import com.easy.query.core.expression.parser.core.base.core.filter.RangePredicate;
import com.easy.query.core.expression.parser.core.base.core.filter.SelfPredicate;
import com.easy.query.core.expression.parser.core.base.core.filter.SubQueryPredicate;
import com.easy.query.core.expression.parser.core.base.core.filter.ValuePredicate;
import com.easy.query.core.expression.parser.core.base.core.filter.ValuesPredicate;
import com.easy.query.core.expression.parser.core.base.scec.core.SQLNativeChainExpressionContextImpl;
import com.easy.query.core.func.SQLFunction;

/**
 * @author xuejiaming
 * @FileName: WherePredicate.java
 * @Description: 文件说明
 * @Date: 2023/2/5 09:09
 */
public interface WherePredicate<T1> extends EntitySQLTableOwner<T1>, SQLFxAvailable, SQLPropertyNative<WherePredicate<T1>>
        , SelfPredicate<T1, WherePredicate<T1>>
        , ValuePredicate<T1, WherePredicate<T1>>
        , FuncValuePredicate<T1, WherePredicate<T1>>
        , FuncColumnPredicate<T1, WherePredicate<T1>>
        , ValuesPredicate<T1, WherePredicate<T1>>
        , RangePredicate<T1, WherePredicate<T1>>
        , LikePredicate<T1, WherePredicate<T1>>
        , AssertPredicate<T1, WherePredicate<T1>>
        , SubQueryPredicate<T1, WherePredicate<T1>> {


    default QueryRuntimeContext getRuntimeContext() {
        return getFilter().getRuntimeContext();
    }
    default WherePredicate<T1> columnFunc(ColumnPropertyFunction columnPropertyFunction, SQLPredicateCompare sqlPredicateCompare, Object val) {
        return columnFunc(true, columnPropertyFunction, sqlPredicateCompare, val);
    }

    WherePredicate<T1> columnFunc(boolean condition, ColumnPropertyFunction columnPropertyFunction, SQLPredicateCompare sqlPredicateCompare, Object val);


    <T2> WherePredicate<T2> then(WherePredicate<T2> sub);

    default WherePredicate<T1> and() {
        return and(true);
    }

    WherePredicate<T1> and(boolean condition);

    default WherePredicate<T1> and(SQLExpression1<WherePredicate<T1>> sqlWherePredicateSQLExpression) {
        return and(true, sqlWherePredicateSQLExpression);
    }

    WherePredicate<T1> and(boolean condition, SQLExpression1<WherePredicate<T1>> sqlWherePredicateSQLExpression);

    /**
     * 采用单参数withOther语法在and内部创建需要的表
     * .where((t1, t2) -> {
     *                      t1.and(x->{
     *                          SQLWherePredicate<BlogEntity> y = x.withOther(t2);
     *                          x.eq(Topic::getStars, 1);
     *                          y.eq(BlogEntity::getOrder, "1");
     *                      });
     *                  })
     * @param t2WherePredicate
     * @param sqlWherePredicateSQLExpression
     * @return
     * @param <T2>
     */
    @Deprecated
    default <T2> WherePredicate<T1> and(WherePredicate<T2> t2WherePredicate, SQLExpression2<WherePredicate<T1>, WherePredicate<T2>> sqlWherePredicateSQLExpression) {
        return and(true, t2WherePredicate, sqlWherePredicateSQLExpression);
    }

    /**
     * 采用单参数withOther语法在and内部创建需要的表
     * .where((t1, t2) -> {
     *                      t1.and(x->{
     *                          SQLWherePredicate<BlogEntity> y = x.withOther(t2);
     *                          x.eq(Topic::getStars, 1);
     *                          y.eq(BlogEntity::getOrder, "1");
     *                      });
     *                  })
     * @param condition
     * @param t2WherePredicate
     * @param sqlWherePredicateSQLExpression
     * @return
     * @param <T2>
     */
    @Deprecated
    <T2> WherePredicate<T1> and(boolean condition, WherePredicate<T2> t2WherePredicate, SQLExpression2<WherePredicate<T1>, WherePredicate<T2>> sqlWherePredicateSQLExpression);

    default WherePredicate<T1> or() {
        return or(true);
    }

    WherePredicate<T1> or(boolean condition);

    default WherePredicate<T1> or(SQLExpression1<WherePredicate<T1>> sqlWherePredicateSQLExpression) {
        return or(true, sqlWherePredicateSQLExpression);
    }

    WherePredicate<T1> or(boolean condition, SQLExpression1<WherePredicate<T1>> sqlWherePredicateSQLExpression);

    /**
     * .where((t1, t2) -> {
     *                      t1.or(x->{
     *                          SQLWherePredicate<BlogEntity> y = x.withOther(t2);
     *                          x.eq(Topic::getStars, 1);
     *                          y.eq(BlogEntity::getOrder, "1");
     *                      });
     *                  })
     * @param t2WherePredicate
     * @param sqlWherePredicateSQLExpression
     * @return
     * @param <T2>
     */
    @Deprecated
    default <T2> WherePredicate<T1> or(WherePredicate<T2> t2WherePredicate, SQLExpression2<WherePredicate<T1>, WherePredicate<T2>> sqlWherePredicateSQLExpression) {
        return or(true, t2WherePredicate, sqlWherePredicateSQLExpression);
    }

    /**
     * .where((t1, t2) -> {
     *                      t1.or(x->{
     *                          SQLWherePredicate<BlogEntity> y = x.withOther(t2);
     *                          x.eq(Topic::getStars, 1);
     *                          y.eq(BlogEntity::getOrder, "1");
     *                      });
     *                  })
     * @param condition
     * @param t2WherePredicate
     * @param sqlWherePredicateSQLExpression
     * @return
     * @param <T2>
     */
    @Deprecated
    <T2> WherePredicate<T1> or(boolean condition, WherePredicate<T2> t2WherePredicate, SQLExpression2<WherePredicate<T1>, WherePredicate<T2>> sqlWherePredicateSQLExpression);
    <T2> WherePredicate<T2> withOther(WherePredicate<T2> wherePredicate);

    @Override
    default WherePredicate<T1> isBank(boolean condition, String property) {
        if (condition) {
            SQLFunction bank = fx().bank(property);
            getFilter().sqlNativeSegment(bank.sqlSegment(getTable()),c->{
                bank.consume(new SQLNativeChainExpressionContextImpl(getTable(),c));
            });
        }
        return this;
    }

    @Override
    default WherePredicate<T1> isNotBank(boolean condition, String property) {
        if (condition) {
            SQLFunction bank = fx().notBank(property);
            getFilter().sqlNativeSegment(bank.sqlSegment(getTable()),c->{
                bank.consume(new SQLNativeChainExpressionContextImpl(getTable(),c));
            });
        }
        return this;
    }
    @Override
    default WherePredicate<T1> isEmpty(boolean condition, String property) {
        if (condition) {
            SQLFunction bank = fx().empty(property);
            getFilter().sqlNativeSegment(bank.sqlSegment(getTable()),c->{
                bank.consume(new SQLNativeChainExpressionContextImpl(getTable(),c));
            });
        }
        return this;
    }

    @Override
    default WherePredicate<T1> isNotEmpty(boolean condition, String property) {
        if (condition) {
            SQLFunction bank = fx().notEmpty(property);
            getFilter().sqlNativeSegment(bank.sqlSegment(getTable()),c->{
                bank.consume(new SQLNativeChainExpressionContextImpl(getTable(),c));
            });
        }
        return this;
    }

}
