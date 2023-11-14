//package com.easy.query.core.basic.jdbc.executor.sql;
//
//import com.easy.query.core.basic.jdbc.conn.EasyConnection;
//import com.easy.query.core.basic.jdbc.executor.ExecutorContext;
//import com.easy.query.core.basic.jdbc.executor.internal.merge.result.StreamResultSet;
//import com.easy.query.core.basic.jdbc.parameter.SQLParameter;
//
//import java.sql.SQLException;
//import java.util.List;
//
///**
// * create time 2023/11/14 17:05
// * 文件说明
// *
// * @author xuejiaming
// */
//public interface JdbcSQLExecutor {
//    StreamResultSet query(ExecutorContext executorContext, EasyConnection easyConnection, String sql, List<SQLParameter> sqlParameters, boolean shardingPrint, boolean replicaPrint) throws SQLException;
//    <T> int insert(ExecutorContext executorContext, EasyConnection easyConnection, String sql, List<T> entities, List<SQLParameter> sqlParameters, boolean fillAutoIncrement, boolean shardingPrint, boolean replicaPrint) throws SQLException;
//    <T> int executeRows(ExecutorContext executorContext, EasyConnection easyConnection, String sql, List<T> entities, List<SQLParameter> sqlParameters, boolean shardingPrint, boolean replicaPrint) throws SQLException;
//    <T> int executeRows(ExecutorContext executorContext, EasyConnection easyConnection, String sql, List<SQLParameter> sqlParameters, boolean shardingPrint, boolean replicaPrint) throws SQLException;
//
//}
