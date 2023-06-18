package com.easy.query.core.basic.api.select.provider;

import com.easy.query.core.expression.parser.core.base.ColumnAsSelector;
import com.easy.query.core.expression.parser.core.base.ColumnResultSelector;
import com.easy.query.core.expression.parser.core.base.ColumnSelector;
import com.easy.query.core.expression.parser.core.base.GroupBySelector;
import com.easy.query.core.expression.parser.core.base.NavigateInclude;
import com.easy.query.core.expression.parser.core.base.WhereAggregatePredicate;
import com.easy.query.core.expression.parser.core.base.WherePredicate;
import com.easy.query.core.expression.parser.core.base.impl.ColumnAsSelectorImpl;
import com.easy.query.core.expression.parser.core.base.impl.ColumnAutoAsSelectorImpl;
import com.easy.query.core.expression.parser.core.base.impl.ColumnResultSelectorImpl;
import com.easy.query.core.expression.parser.core.base.impl.ColumnSelectorImpl;
import com.easy.query.core.expression.parser.core.base.impl.GroupBySelectorImpl;
import com.easy.query.core.expression.parser.core.base.impl.NavigateIncludeImpl;
import com.easy.query.core.expression.parser.core.base.impl.OrderColumnSelectorImpl;
import com.easy.query.core.expression.parser.core.base.impl.WhereAggregatePredicateImpl;
import com.easy.query.core.expression.parser.core.base.impl.WherePredicateImpl;
import com.easy.query.core.expression.segment.builder.SQLBuilderSegment;
import com.easy.query.core.expression.sql.builder.EntityQueryExpressionBuilder;
import com.easy.query.core.util.EasyUtil;

/**
 * create time 2023/5/20 11:18
 * 文件说明
 *
 * @author xuejiaming
 */
public class SQLExpressionProviderImpl<TEntity> implements SQLExpressionProvider<TEntity> {

    private final EntityQueryExpressionBuilder entityQueryExpressionBuilder;
    private final int index;
    private GroupBySelectorImpl<TEntity> group;
    private OrderColumnSelectorImpl<TEntity> order;
    private WherePredicateImpl<TEntity> where;
    private NavigateIncludeImpl<TEntity> navigateInclude;
    private WherePredicateImpl<TEntity> allPredicate;
    private WhereAggregatePredicateImpl<TEntity> having;
    private WherePredicateImpl<TEntity> on;

    public SQLExpressionProviderImpl(int index, EntityQueryExpressionBuilder entityQueryExpressionBuilder) {
        this.index = index;
        this.entityQueryExpressionBuilder = entityQueryExpressionBuilder;
    }

    @Override
    public GroupBySelector<TEntity> getGroupColumnSelector() {
        if (group == null) {
            group = new GroupBySelectorImpl<>(index, entityQueryExpressionBuilder);
        }
        return group;
    }

    @Override
    public OrderColumnSelectorImpl<TEntity> getOrderColumnSelector(boolean asc) {
        if (order == null) {
            order = new OrderColumnSelectorImpl<>(index, entityQueryExpressionBuilder);
        }
        order.setAsc(asc);
        return order;
    }

    @Override
    public WherePredicate<TEntity> getWherePredicate() {
        if (where == null) {
            where = new WherePredicateImpl<>(index, entityQueryExpressionBuilder, entityQueryExpressionBuilder.getWhere());
        }
        return where;
    }

    @Override
    public NavigateInclude<TEntity> getNavigateInclude() {
        if(navigateInclude==null){
            navigateInclude=new NavigateIncludeImpl<>(index,entityQueryExpressionBuilder);
        }
        return navigateInclude;
    }

    @Override
    public WherePredicate<TEntity> getAllWherePredicate() {
        if (allPredicate == null) {
            allPredicate = new WherePredicateImpl<>(index, entityQueryExpressionBuilder, entityQueryExpressionBuilder.getAllPredicate(), true);
        }
        return allPredicate;
    }

    @Override
    public WhereAggregatePredicate<TEntity> getAggregatePredicate() {
        if (having == null) {
            having = new WhereAggregatePredicateImpl<>(index, entityQueryExpressionBuilder, entityQueryExpressionBuilder.getHaving());
        }
        return having;
    }

    @Override
    public WherePredicate<TEntity> getOnPredicate() {
        if (on == null) {
            on = new WherePredicateImpl<>(index, entityQueryExpressionBuilder, EasyUtil.getCurrentPredicateTable(entityQueryExpressionBuilder).getOn());
        }
        return on;
    }

    @Override
    public ColumnSelector<TEntity> getColumnSelector(SQLBuilderSegment sqlSegment0Builder) {
        return new ColumnSelectorImpl<>(index, entityQueryExpressionBuilder, sqlSegment0Builder);
    }

    @Override
    public <TR> ColumnAsSelector<TEntity, TR> getColumnAsSelector(SQLBuilderSegment sqlSegment0Builder, Class<TR> resultClass) {
        return new ColumnAsSelectorImpl<>(index, entityQueryExpressionBuilder, sqlSegment0Builder, resultClass);
    }

    @Override
    public <TR> ColumnAsSelector<TEntity, TR> getAutoColumnAsSelector(SQLBuilderSegment sqlSegment0Builder, Class<TR> resultClass) {
        return new ColumnAutoAsSelectorImpl<>(index, entityQueryExpressionBuilder, sqlSegment0Builder, resultClass);
    }

    @Override
    public <TR> ColumnResultSelector<TEntity> getColumnResultSelector(SQLBuilderSegment sqlSegment0Builder) {
        return new ColumnResultSelectorImpl<TEntity>(index, entityQueryExpressionBuilder, sqlSegment0Builder);
    }
}
