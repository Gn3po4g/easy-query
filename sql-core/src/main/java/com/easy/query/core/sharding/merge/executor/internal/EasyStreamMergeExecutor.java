package com.easy.query.core.sharding.merge.executor.internal;

import com.easy.query.core.basic.jdbc.con.EasyConnection;
import com.easy.query.core.basic.jdbc.executor.ExecutorContext;
import com.easy.query.core.basic.jdbc.parameter.SQLParameter;
import com.easy.query.core.exception.EasyQueryException;
import com.easy.query.core.sharding.merge.StreamMergeContext;
import com.easy.query.core.sharding.merge.executor.common.CommandExecuteUnit;
import com.easy.query.core.sharding.merge.executor.common.SqlUnit;
import com.easy.query.core.sharding.merge.executor.merger.DefaultStreamShardingMerger;
import com.easy.query.core.sharding.merge.executor.merger.DefaultStreamShardingMerger0;
import com.easy.query.core.sharding.merge.impl.DefaultStreamResult;
import com.easy.query.core.util.JDBCExecutorUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * create time 2023/4/14 11:34
 * 文件说明
 *
 * @author xuejiaming
 */
public class EasyStreamMergeExecutor extends AbstractExecutor<ExecuteResult> {

    public EasyStreamMergeExecutor(StreamMergeContext streamMergeContext) {
        super(streamMergeContext);
    }

    @Override
    protected ExecuteResult executeCommandUnit(CommandExecuteUnit commandExecuteUnit) {

        ExecutorContext executorContext = streamMergeContext.getExecutorContext();
        EasyConnection easyConnection = commandExecuteUnit.getEasyConnection();
        SqlUnit sqlUnit = commandExecuteUnit.getExecutionUnit().getSqlUnit();
        String sql = sqlUnit.getSql();
        List<SQLParameter> parameters = sqlUnit.getParameters();
        return JDBCExecutorUtil.query(executorContext,easyConnection,sql,parameters);
    }
    @Override
    public ShardingMerger<ExecuteResult> getShardingMerger() {
        return DefaultStreamShardingMerger.getInstance();
    }
}
