package com.easy.query.core.sharding.rewrite;

import com.easy.query.core.context.QueryRuntimeContext;
import com.easy.query.core.enums.EasyBehaviorEnum;
import com.easy.query.core.enums.ExecuteMethodEnum;
import com.easy.query.core.enums.MergeBehaviorEnum;
import com.easy.query.core.exception.EasyQueryInvalidOperationException;
import com.easy.query.core.expression.executor.parser.PrepareParseResult;
import com.easy.query.core.expression.executor.parser.QueryPrepareParseResult;
import com.easy.query.core.expression.executor.parser.SequenceParseResult;
import com.easy.query.core.expression.func.AggregationType;
import com.easy.query.core.expression.parser.core.available.TableAvailable;
import com.easy.query.core.expression.segment.ColumnSegment;
import com.easy.query.core.expression.segment.FuncColumnSegment;
import com.easy.query.core.expression.segment.GroupByColumnSegment;
import com.easy.query.core.expression.segment.OrderBySegment;
import com.easy.query.core.expression.segment.SQLSegment;
import com.easy.query.core.expression.segment.builder.ProjectSQLBuilderSegment;
import com.easy.query.core.expression.segment.factory.SQLSegmentFactory;
import com.easy.query.core.expression.segment.impl.SQLFunctionColumnSegmentImpl;
import com.easy.query.core.expression.segment.impl.SQLNativeSegmentImpl;
import com.easy.query.core.expression.segment.index.EntitySegmentComparer;
import com.easy.query.core.expression.sql.builder.ExpressionContext;
import com.easy.query.core.expression.sql.expression.EntityQuerySQLExpression;
import com.easy.query.core.func.SQLFunctionTranslateImpl;
import com.easy.query.core.func.def.DistinctDefaultSQLFunction;
import com.easy.query.core.metadata.EntityMetadata;
import com.easy.query.core.metadata.ShardingInitConfig;
import com.easy.query.core.metadata.ShardingSequenceConfig;
import com.easy.query.core.sharding.manager.ShardingQueryCountManager;
import com.easy.query.core.sharding.router.RouteContext;
import com.easy.query.core.sharding.router.RouteUnit;
import com.easy.query.core.sharding.router.ShardingRouteResult;
import com.easy.query.core.util.EasyBitwiseUtil;
import com.easy.query.core.util.EasyClassUtil;
import com.easy.query.core.util.EasyCollectionUtil;
import com.easy.query.core.util.EasyMapUtil;
import com.easy.query.core.util.EasySQLSegmentUtil;
import com.easy.query.core.util.EasyShardingUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * create time 2023/4/20 14:56
 * 文件说明
 *
 * @author xuejiaming
 */
public class DefaultRewriteContextFactory implements RewriteContextFactory {
    private final SQLSegmentFactory sqlSegmentFactory;

    public DefaultRewriteContextFactory(SQLSegmentFactory sqlSegmentFactory) {

        this.sqlSegmentFactory = sqlSegmentFactory;
    }

    @Override
    public RewriteContext rewriteShardingExpression(PrepareParseResult prepareParseResult, RouteContext routeContext) {
        if (prepareParseResult instanceof QueryPrepareParseResult) {
            return rewriteShardingQueryExpression((QueryPrepareParseResult) prepareParseResult, routeContext);
        }
        return createDefaultRewriteContext(prepareParseResult, routeContext);
    }

    public RewriteContext rewriteShardingQueryExpression(QueryPrepareParseResult queryPrepareParseResult, RouteContext routeContext) {

        EntityQuerySQLExpression easyEntityPredicateSQLExpression = queryPrepareParseResult.getEntityPredicateSQLExpression();
        QueryRuntimeContext runtimeContext = easyEntityPredicateSQLExpression.getRuntimeContext();
        ExpressionContext expressionContext = easyEntityPredicateSQLExpression.getExpressionMetadata().getExpressionContext();
        if (!routeContext.getShardingRouteResult().isCrossDataSource() && queryPrepareParseResult.getExecutorContext().isQuery() && expressionContext.getBehavior().hasBehavior(EasyBehaviorEnum.SHARDING_UNION_ALL)) {
            return createDefaultRewriteContext(queryPrepareParseResult, routeContext);
        }
        //添加默认排序字段,并且添加默认排序字段到select 如果不添加那么streamResultSet将无法进行order排序获取
        if (EasySQLSegmentUtil.isEmpty(easyEntityPredicateSQLExpression.getOrder())
                &&
                (ExecuteMethodEnum.LIST == queryPrepareParseResult.getExecutorContext().getExecuteMethod()
                        ||
                        ExecuteMethodEnum.FIRST == queryPrepareParseResult.getExecutorContext().getExecuteMethod()
                        ||
                        ExecuteMethodEnum.SINGLE == queryPrepareParseResult.getExecutorContext().getExecuteMethod())) {
            SequenceParseResult sequenceParseResult = queryPrepareParseResult.getSequenceParseResult();
            if (sequenceParseResult != null) {
                TableAvailable table = sequenceParseResult.getTable();
                if (EasyCollectionUtil.any(easyEntityPredicateSQLExpression.getTables(), t -> Objects.equals(t.getEntityTable(), table))) {
                    EntityMetadata entityMetadata = table.getEntityMetadata();
                    ShardingInitConfig shardingInitConfig = entityMetadata.getShardingInitConfig();
                    ShardingSequenceConfig shardingSequenceConfig = shardingInitConfig.getShardingSequenceConfig();
                    if (shardingSequenceConfig != null) {
                        boolean reverse = sequenceParseResult.isReverse();
                        String firstSequenceProperty = shardingSequenceConfig.getFirstSequencePropertyOrNull();
                        if (firstSequenceProperty != null) {
                            OrderBySegment orderByColumnSegment = sqlSegmentFactory.createOrderByColumnSegment(table, firstSequenceProperty, expressionContext, !reverse);
                            easyEntityPredicateSQLExpression.getOrder().append(orderByColumnSegment);
                            EntitySegmentComparer comparer = new EntitySegmentComparer(entityMetadata.getEntityClass(), firstSequenceProperty);
                            if (!easyEntityPredicateSQLExpression.getProjects().contains(comparer)) {
                                ColumnSegment columnSegment = sqlSegmentFactory.createSelectColumnSegment(table, firstSequenceProperty, expressionContext, null);
                                easyEntityPredicateSQLExpression.getProjects().append(columnSegment);
                            }
                        }
                    }
                }
            }
        }
        //如果当前表达式存在group 并且不为空
        if (EasySQLSegmentUtil.isNotEmpty(easyEntityPredicateSQLExpression.getGroup())) {
            boolean hasAvg = false;
            //分组重写的如果存在count avg sum那么就会存储
            Map<GroupRewriteStatus, GroupRewriteStatus> groupRewriteStatusMap = new LinkedHashMap<>(easyEntityPredicateSQLExpression.getProjects().getSQLSegments().size());
            //group的字段必须要全部存在于select中
            //遍历所有的表达式
            for (SQLSegment groupSQLSegment : easyEntityPredicateSQLExpression.getGroup().getSQLSegments()) {
                if (!(groupSQLSegment instanceof ColumnSegment)) {
                    throw new UnsupportedOperationException("sharding rewrite group not implement ColumnSegment:" + EasyClassUtil.getInstanceSimpleName(groupSQLSegment));
                }
                ColumnSegment groupColumnSegment = (ColumnSegment) groupSQLSegment;

                boolean addToProjection = true;
                for (SQLSegment sqlSegment : easyEntityPredicateSQLExpression.getProjects().getSQLSegments()) {
                    if (sqlSegment instanceof FuncColumnSegment) {
                        FuncColumnSegment aggregationColumnSegment = (FuncColumnSegment) sqlSegment;
                        //是否存在avg
                        if (!hasAvg) {
                            hasAvg = Objects.equals(AggregationType.AVG, aggregationColumnSegment.getAggregationType());
                        }
                        GroupRewriteStatus groupRewriteStatusKey = new GroupRewriteStatus(aggregationColumnSegment.getTable(), aggregationColumnSegment.getPropertyName());
                        GroupRewriteStatus groupRewriteStatus = EasyMapUtil.computeIfAbsent(groupRewriteStatusMap, groupRewriteStatusKey, k -> groupRewriteStatusKey);

                        GroupAvgBehaviorEnum groupAvgBehavior = GroupAvgBehaviorEnum.getGroupAvgBehavior(aggregationColumnSegment.getAggregationType());
                        if (groupAvgBehavior != null) {
                            groupRewriteStatus.removeBehavior(groupAvgBehavior);//有count了把count移除,有sum了吧sum移除有avg把avg移除
                        }

                        continue;
                    }
                    if (!(sqlSegment instanceof ColumnSegment)) {
                        throw new UnsupportedOperationException("sharding rewrite projection not implement ColumnSegment:" + EasyClassUtil.getInstanceSimpleName(sqlSegment));
                    }
                    ColumnSegment columnSegment = (ColumnSegment) sqlSegment;
                    //
                    if (Objects.equals(groupColumnSegment.getTable(), columnSegment.getTable())) {
                        if (Objects.equals(groupColumnSegment.getPropertyName(), columnSegment.getPropertyName())) {
                            if (addToProjection) {
                                addToProjection = false;
                            }
                        }
                    }
                }
                if (addToProjection) {
                    easyEntityPredicateSQLExpression.getProjects().append(groupColumnSegment.cloneSQLColumnSegment());
                }
            }

            List<SQLSegment> groupSQLSegments = easyEntityPredicateSQLExpression.getGroup().getSQLSegments();
            List<SQLSegment> orderSQLSegments = easyEntityPredicateSQLExpression.getOrder().getSQLSegments();
            //group by或者order by小的那个是另一个的startsWith即可
            boolean startsWithGroupByAndOrderBy = EasyShardingUtil.isGroupByAndOrderByStartsWith(groupSQLSegments, orderSQLSegments);
            queryPrepareParseResult.setStartsWithGroupByInOrderBy(startsWithGroupByAndOrderBy);
            //如果是的情况下
            if (startsWithGroupByAndOrderBy) {
                int orderBySize = orderSQLSegments.size();
                int groupBySize = groupSQLSegments.size();
                if (orderBySize < groupBySize) {
                    for (int i = orderBySize; i < groupBySize; i++) {
                        GroupByColumnSegment groupByColumnSegment = (GroupByColumnSegment) groupSQLSegments.get(i);
                        if (groupByColumnSegment.getTable() == null || groupByColumnSegment.getPropertyName() == null) {
                            throw new EasyQueryInvalidOperationException("not found group column table or property");
                        }
                        easyEntityPredicateSQLExpression.getOrder().append(groupByColumnSegment.createOrderByColumnSegment(true));
                    }
                }
            }
            //判断sum count avg如果存在avg那么前面两个不能缺
            if (hasAvg) {
                for (Map.Entry<GroupRewriteStatus, GroupRewriteStatus> groupRewriteStatusKv : groupRewriteStatusMap.entrySet()) {
                    GroupRewriteStatus rewriteStatusKvKey = groupRewriteStatusKv.getKey();
                    //如果avg还有说明本次group没有avg
                    if (rewriteStatusKvKey.hasBehavior(GroupAvgBehaviorEnum.AVG)) {
                        continue;
                    }
                    //如果存在avg那么分片必须要存在count或者sum不然无法计算avg
                    if (rewriteStatusKvKey.hasBehavior(GroupAvgBehaviorEnum.COUNT)) {
                        DistinctDefaultSQLFunction sqlFunction = runtimeContext.fx().count(s -> s.column(rewriteStatusKvKey.getTable(), rewriteStatusKvKey.getPropertyName()));
                        SQLSegment sqlSegment = new SQLFunctionTranslateImpl(sqlFunction)
                                .toSQLSegment(expressionContext, rewriteStatusKvKey.getTable(), runtimeContext, null);
                        FuncColumnSegment funcColumnSegment = new SQLFunctionColumnSegmentImpl(rewriteStatusKvKey.getTable(), null, runtimeContext, sqlSegment, sqlFunction.getAggregationType(), rewriteStatusKvKey.getPropertyName() + "RewriteCount");
                        easyEntityPredicateSQLExpression.getProjects().append(funcColumnSegment);
                    }
                    if (rewriteStatusKvKey.hasBehavior(GroupAvgBehaviorEnum.SUM)) {

                        DistinctDefaultSQLFunction sqlFunction = runtimeContext.fx().sum(s -> s.column(rewriteStatusKvKey.getTable(), rewriteStatusKvKey.getPropertyName()));
                        SQLSegment sqlSegment = new SQLFunctionTranslateImpl(sqlFunction)
                                .toSQLSegment(expressionContext, rewriteStatusKvKey.getTable(), runtimeContext, null);
                        FuncColumnSegment funcColumnSegment = new SQLFunctionColumnSegmentImpl(rewriteStatusKvKey.getTable(), null, runtimeContext, sqlSegment, sqlFunction.getAggregationType(), rewriteStatusKvKey.getPropertyName() + "RewriteSum");

                        easyEntityPredicateSQLExpression.getProjects().append(funcColumnSegment);
                    }
                }
            }
        } else {
            //distinct的时候并且没有aggregate projects的都放到group上
            if (easyEntityPredicateSQLExpression.isDistinct()) {
                ProjectSQLBuilderSegment projects = (ProjectSQLBuilderSegment) easyEntityPredicateSQLExpression.getProjects();
                if (!projects.hasAggregateColumns()) {
                    for (SQLSegment sqlSegment : easyEntityPredicateSQLExpression.getProjects().getSQLSegments()) {
                        if (sqlSegment instanceof ColumnSegment) {
                            easyEntityPredicateSQLExpression.getGroup().append(sqlSegment);
                        }
                    }
                }
            }
        }


        int mergeBehavior = EasyShardingUtil.parseMergeBehavior(queryPrepareParseResult, easyEntityPredicateSQLExpression, routeContext);
        if (EasyBitwiseUtil.hasBit(mergeBehavior, MergeBehaviorEnum.PAGINATION.getCode())) {
            return createPaginationRewriteContext(mergeBehavior, queryPrepareParseResult, easyEntityPredicateSQLExpression, routeContext);
        }
        if (EasyBitwiseUtil.hasBit(mergeBehavior, MergeBehaviorEnum.SEQUENCE_COUNT.getCode())) {
            ShardingQueryCountManager shardingQueryCountManager = runtimeContext.getShardingQueryCountManager();
            List<Long> countResult = shardingQueryCountManager.getCountResult();
            List<RewriteRouteUnit> sequenceCountRewriteRouteUnits = EasyShardingUtil.getSequenceCountRewriteRouteUnits(queryPrepareParseResult, routeContext, countResult);
            ShardingRouteResult shardingRouteResult = routeContext.getShardingRouteResult();
            return new RewriteContext(mergeBehavior, queryPrepareParseResult, sequenceCountRewriteRouteUnits, shardingRouteResult.isCrossDataSource(), shardingRouteResult.isCrossTable(), shardingRouteResult.isSequenceQuery(), false);
        }

        return createDefaultRewriteContext(mergeBehavior, queryPrepareParseResult, routeContext);
    }

    /**
     * 创建默认的重写上下文
     *
     * @param mergeBehavior
     * @param prepareParseResult
     * @param routeContext
     * @return
     */
    private RewriteContext createDefaultRewriteContext(int mergeBehavior, PrepareParseResult prepareParseResult, RouteContext routeContext) {
        ShardingRouteResult shardingRouteResult = routeContext.getShardingRouteResult();
        List<RewriteRouteUnit> rewriteRouteUnits = createDefaultRewriteRouteUnit(routeContext);
        return new RewriteContext(mergeBehavior, prepareParseResult, rewriteRouteUnits, shardingRouteResult.isCrossDataSource(), shardingRouteResult.isCrossTable(), shardingRouteResult.isSequenceQuery(), false);
    }

    private RewriteContext createDefaultRewriteContext(PrepareParseResult prepareParseResult, RouteContext routeContext) {
        return createDefaultRewriteContext(MergeBehaviorEnum.DEFAULT.getCode(), prepareParseResult, routeContext);
    }

    /**
     * 创建默认的重写路由单元
     *
     * @param routeContext
     * @return
     */
    private List<RewriteRouteUnit> createDefaultRewriteRouteUnit(RouteContext routeContext) {
        ShardingRouteResult shardingRouteResult = routeContext.getShardingRouteResult();
        List<RouteUnit> routeUnits = shardingRouteResult.getRouteUnits();
        ArrayList<RewriteRouteUnit> rewriteRouteUnits = new ArrayList<>(routeUnits.size());
        for (RouteUnit routeUnit : routeUnits) {
            rewriteRouteUnits.add(new DefaultRewriteRouteUnit(routeUnit));
        }
        return rewriteRouteUnits;
    }

    /**
     * 创建分页重写上下文
     *
     * @param mergeBehavior
     * @param queryPrepareParseResult
     * @param easyQuerySQLExpression
     * @param routeContext
     * @return
     */
    private RewriteContext createPaginationRewriteContext(int mergeBehavior, QueryPrepareParseResult queryPrepareParseResult, EntityQuerySQLExpression easyQuerySQLExpression, RouteContext routeContext) {
        List<RewriteRouteUnit> rewriteRouteUnits = getPaginationRewriteRouteUnitsAndRewriteQuerySQLExpression(mergeBehavior, queryPrepareParseResult, easyQuerySQLExpression, routeContext);
        ShardingRouteResult shardingRouteResult = routeContext.getShardingRouteResult();
        //是否需要反向排序
        boolean reverseMerge = !EasyBitwiseUtil.hasBit(mergeBehavior, MergeBehaviorEnum.SEQUENCE_PAGINATION.getCode()) && EasyBitwiseUtil.hasBit(mergeBehavior, MergeBehaviorEnum.REVERSE_PAGINATION.getCode());
        return new RewriteContext(mergeBehavior, queryPrepareParseResult, rewriteRouteUnits, shardingRouteResult.isCrossDataSource(), shardingRouteResult.isCrossTable(), shardingRouteResult.isSequenceQuery(), reverseMerge);
    }

    /**
     * 获取分页重新路由单元和重写分页sql表达式
     *
     * @param mergeBehavior
     * @param queryPrepareParseResult
     * @param easyQuerySQLExpression
     * @param routeContext
     * @return
     */
    private List<RewriteRouteUnit> getPaginationRewriteRouteUnitsAndRewriteQuerySQLExpression(int mergeBehavior, QueryPrepareParseResult queryPrepareParseResult, EntityQuerySQLExpression easyQuerySQLExpression, RouteContext routeContext) {

        QueryRuntimeContext runtimeContext = queryPrepareParseResult.getExecutorContext().getRuntimeContext();
        if (EasyBitwiseUtil.hasBit(mergeBehavior, MergeBehaviorEnum.SEQUENCE_PAGINATION.getCode())) {
            ShardingQueryCountManager shardingQueryCountManager = runtimeContext.getShardingQueryCountManager();
            List<Long> countResult = shardingQueryCountManager.getCountResult();
            List<RewriteRouteUnit> rewriteRouteUnits = EasyShardingUtil.getSequencePaginationRewriteRouteUnits(queryPrepareParseResult, routeContext, countResult);
            rewritePagination(easyQuerySQLExpression);
            return rewriteRouteUnits;
        }
        if (EasyBitwiseUtil.hasBit(mergeBehavior, MergeBehaviorEnum.REVERSE_PAGINATION.getCode())) {
            ShardingQueryCountManager shardingQueryCountManager = runtimeContext.getShardingQueryCountManager();
            List<Long> countResult = shardingQueryCountManager.getCountResult();
            long total = EasyCollectionUtil.sumLong(countResult, o -> o);
            long originalOffset = queryPrepareParseResult.getOriginalOffset();
            long originalRows = queryPrepareParseResult.getOriginalRows();
            long realOffset = total - originalOffset - originalRows;
            long realRows = realOffset + originalRows;
            ShardingRouteResult shardingRouteResult = routeContext.getShardingRouteResult();
            List<RouteUnit> routeUnits = shardingRouteResult.getRouteUnits();
            ArrayList<RewriteRouteUnit> rewriteRouteUnits = new ArrayList<>(routeUnits.size());
            for (RouteUnit routeUnit : routeUnits) {
                rewriteRouteUnits.add(new ReversePaginationRewriteRouteUnit(0L, realRows, routeUnit));
            }
            rewriteReversePagination(easyQuerySQLExpression, realOffset);
            return rewriteRouteUnits;
        }
        rewritePagination(easyQuerySQLExpression);
        return createDefaultRewriteRouteUnit(routeContext);
    }

    private void rewritePagination(EntityQuerySQLExpression easyQuerySQLExpression) {

        if (easyQuerySQLExpression.hasLimit()) {
            long rows = easyQuerySQLExpression.getRows();
            long offset = easyQuerySQLExpression.getOffset();
            if (offset > 0) {
                easyQuerySQLExpression.setOffset(0);
            }
            easyQuerySQLExpression.setRows(offset + rows);
        }
    }

    private void rewriteReversePagination(EntityQuerySQLExpression easyQuerySQLExpression, long realOffset) {

        if (easyQuerySQLExpression.hasLimit()) {
            long rows = easyQuerySQLExpression.getRows();
            long offset = easyQuerySQLExpression.getOffset();
            if (offset > 0) {
                easyQuerySQLExpression.setOffset(0);
            }
            easyQuerySQLExpression.setRows(realOffset + rows);
        }
    }
}
