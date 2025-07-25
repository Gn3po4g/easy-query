package com.easy.query.core.basic.jdbc.executor.internal.sharding.merger.impl;

import com.easy.query.core.basic.jdbc.executor.internal.sharding.merger.abstraction.AbstractShardingMerger;
import com.easy.query.core.basic.jdbc.executor.internal.result.QueryExecuteResult;
import com.easy.query.core.basic.jdbc.executor.internal.result.impl.DefaultQueryExecuteResult;
import com.easy.query.core.enums.MergeBehaviorEnum;
import com.easy.query.core.exception.EasyQueryInvalidOperationException;
import com.easy.query.core.sharding.context.StreamMergeContext;
import com.easy.query.core.basic.jdbc.executor.internal.merge.result.StreamResultSet;
import com.easy.query.core.basic.jdbc.executor.internal.merge.result.impl.memory.single.EasyAllInMemoryStreamMergeResultSet;
import com.easy.query.core.basic.jdbc.executor.internal.merge.result.impl.memory.single.EasyAnyInMemoryStreamMergeResultSet;
import com.easy.query.core.basic.jdbc.executor.internal.merge.result.impl.memory.EasyInMemoryGroupByOrderStreamMergeResultSet;
import com.easy.query.core.basic.jdbc.executor.internal.merge.result.impl.memory.EasyInMemoryStreamMergeResultSet;
import com.easy.query.core.basic.jdbc.executor.internal.merge.result.impl.EasyGroupByOrderStreamMergeResultSet;
import com.easy.query.core.basic.jdbc.executor.internal.merge.result.impl.EasyMultiOrderStreamMergeResultSet;
import com.easy.query.core.basic.jdbc.executor.internal.merge.result.impl.EasyMultiStreamMergeResultSet;
import com.easy.query.core.basic.jdbc.executor.internal.merge.result.impl.EasyPaginationStreamMergeResultSet;
import com.easy.query.core.basic.jdbc.executor.internal.merge.result.impl.memory.single.EasyCountInMemoryStreamMergeResultSet;
import com.easy.query.core.util.EasyCollectionUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * create time 2023/4/20 22:35
 * 文件说明
 *
 * @author xuejiaming
 */
public class QueryStreamShardingMerger extends AbstractShardingMerger<QueryExecuteResult> {
    public static final QueryStreamShardingMerger INSTANCE = new QueryStreamShardingMerger();

    protected StreamResultSet streamMergeToSingle(StreamMergeContext streamMergeContext, List<StreamResultSet> streamResults) throws SQLException {

//        if(EasyCollectionUtil.isSingle(streamResults)){
//            return EasyCollectionUtil.first(streamResults);
//        }
        StreamResultSet streamResultSet = multiStreamMerge(streamMergeContext, streamResults);
        if (!streamMergeContext.hasBehavior(MergeBehaviorEnum.PAGINATION)||streamMergeContext.hasBehavior(MergeBehaviorEnum.SEQUENCE_PAGINATION)) {
            return streamResultSet;
        }
        return new EasyPaginationStreamMergeResultSet(streamMergeContext, streamResultSet);

    }

    protected StreamResultSet multiStreamMerge(StreamMergeContext streamMergeContext, List<StreamResultSet> streamResults) throws SQLException {
        if (streamMergeContext.hasBehavior(MergeBehaviorEnum.ALL)) {
            return new EasyAllInMemoryStreamMergeResultSet(streamMergeContext, streamResults);
        }
        if (streamMergeContext.hasBehavior(MergeBehaviorEnum.ANY)) {
            return new EasyAnyInMemoryStreamMergeResultSet(streamMergeContext, streamResults);
        }
        if (streamMergeContext.hasBehavior(MergeBehaviorEnum.COUNT)) {
            return new EasyCountInMemoryStreamMergeResultSet(streamMergeContext, streamResults);
        }
        if (streamMergeContext.hasBehavior(MergeBehaviorEnum.GROUP)) {
            return groupMerge(streamMergeContext, streamResults);
        }
        if (streamMergeContext.hasBehavior(MergeBehaviorEnum.ORDER)) {
            return new EasyMultiOrderStreamMergeResultSet(streamMergeContext, streamResults);
        }
        return new EasyMultiStreamMergeResultSet(streamMergeContext, streamResults);
    }

    private StreamResultSet groupMerge(StreamMergeContext streamMergeContext, List<StreamResultSet> streamResults) throws SQLException {

        if (streamMergeContext.hasBehavior(MergeBehaviorEnum.STREAM_GROUP)) {
            return new EasyGroupByOrderStreamMergeResultSet(streamMergeContext, streamResults);
        } else {
            return new EasyInMemoryGroupByOrderStreamMergeResultSet(streamMergeContext, streamResults);
        }
    }

    @Override
    public QueryExecuteResult streamMerge(StreamMergeContext streamMergeContext, Collection<QueryExecuteResult> parallelResults) throws SQLException {

        /**
         * 如果当前不是分片那么也需要聚合操作,哪怕是单个的因为分页表达式会重写
         */
        if (EasyCollectionUtil.isSingle(parallelResults) && !streamMergeContext.isSharding()) {
            return EasyCollectionUtil.firstOrNull(parallelResults);
        }
        List<StreamResultSet> streamResults = EasyCollectionUtil.select(parallelResults, (o, i) -> o.getStreamResultSet());
        StreamResultSet streamResultSet = streamMergeToSingle(streamMergeContext, streamResults);
        return new DefaultQueryExecuteResult(streamResultSet);
    }

    protected StreamResultSet streamInMemoryMerge(StreamMergeContext streamMergeContext, List<StreamResultSet> parallelResults) throws SQLException {

        StreamResultSet streamResultSet = multiStreamMerge(streamMergeContext, parallelResults);
        if (!streamMergeContext.hasBehavior(MergeBehaviorEnum.PAGINATION)||streamMergeContext.hasBehavior(MergeBehaviorEnum.SEQUENCE_PAGINATION)) {
            return streamResultSet;
        }
//        long rewriteOffset = streamMergeContext.getRewriteOffset();
//        long rewriteRows = streamMergeContext.getRewriteRows();
//        if(streamMergeContext.isReverseMerge()){
//            rewriteRows=streamMergeContext.getMergeOffset()+streamMergeContext.getRows();
//        }
        return new EasyPaginationStreamMergeResultSet(streamMergeContext, streamResultSet,streamMergeContext.getRewriteOffset(), streamMergeContext.getRewriteRows());

    }

    @Override
    public void inMemoryMerge(StreamMergeContext streamMergeContext, Collection<QueryExecuteResult> beforeInMemoryResults, Collection<QueryExecuteResult> parallelResults) throws SQLException {
        int previewResultsSize = beforeInMemoryResults.size();
        if (previewResultsSize > 1) {
            throw new EasyQueryInvalidOperationException("in memory merge has more element in results");
        }
        if (EasyCollectionUtil.isEmpty(parallelResults)) {
            return;
        }
        ArrayList<StreamResultSet> mergeList = new ArrayList<>(parallelResults.size() + previewResultsSize);
        if (previewResultsSize == 1) {
            mergeList.add(EasyCollectionUtil.firstOrNull(beforeInMemoryResults).getStreamResultSet());
        }
        for (QueryExecuteResult parallelResult : parallelResults) {
            mergeList.add(parallelResult.getStreamResultSet());
        }
        StreamResultSet combineStreamMergeResultSet = streamInMemoryMerge(streamMergeContext, mergeList);
        EasyInMemoryStreamMergeResultSet easyInMemoryStreamMergeResultSet = new EasyInMemoryStreamMergeResultSet(streamMergeContext, Collections.singletonList(combineStreamMergeResultSet));
        beforeInMemoryResults.clear();
        beforeInMemoryResults.add(new DefaultQueryExecuteResult(easyInMemoryStreamMergeResultSet));
    }
}
