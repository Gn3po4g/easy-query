package org.easy.query.core.abstraction.sql;

import org.easy.query.core.abstraction.lambda.Property;
import org.easy.query.core.abstraction.lambda.SqlExpression;
import org.easy.query.core.abstraction.sql.base.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @FileName: Select0.java
 * @Description: 文件说明
 * @Date: 2023/2/6 21:28
 * @Created by xuejiaming
 */
public interface Select0<T1, TChain> {
    long count();
    long countDistinct(SqlExpression<SqlColumnSelector<T1>> selectExpression);

    boolean any();

    /**
     * 防止溢出
     * @param column
     * @return
     * @param <TMember>
     */
    default  <TMember extends Number> BigDecimal sumBigDecimalOrNull(Property<T1, TMember> column){
        return sumBigDecimalOrDefault(column,null);
    }

    /**
     * 防止溢出
     * @param column
     * @return
     * @param <TMember>
     */
    default  <TMember extends Number> BigDecimal sumBigDecimalNotNull(Property<T1, TMember> column){
        return sumBigDecimalOrDefault(column,BigDecimal.ZERO);
    }
    <TMember extends Number> BigDecimal sumBigDecimalOrDefault(Property<T1, TMember> column,BigDecimal def);

   default  <TMember extends Number> TMember sumOrNull(Property<T1, TMember> column){
       return sumOrDefault(column,null);
   }
    <TMember extends Number> TMember sumOrDefault(Property<T1, TMember> column,TMember def);

    default <TMember> TMember maxOrNull(Property<T1, TMember> column) {
        return maxOrDefault(column, null);
    }

    <TMember> TMember maxOrDefault(Property<T1, TMember> column, TMember def);
    default <TMember> TMember minOrNull(Property<T1, TMember> column) {
        return minOrDefault(column, null);
    }

    <TMember> TMember minOrDefault(Property<T1, TMember> column, TMember def);

    default <TMember> TMember avgOrNull(Property<T1, TMember> column) {
        return avgOrDefault(column, null);
    }

    <TMember> TMember avgOrDefault(Property<T1, TMember> column, TMember def);


    T1 firstOrNull();

    /**
     * 查询某些字段
     *
     * @param selectExpression
     * @return
     */
    T1 firstOrNull(SqlExpression<SqlColumnSelector<T1>> selectExpression);

    /**
     * 结果转换成某个对象
     *
     * @param resultClass
     * @param <TR>
     * @return
     */
    <TR> TR firstOrNull(Class<TR> resultClass);

    /**
     * 转换成某个对象并且映射字段
     *
     * @param resultClass
     * @param selectExpression
     * @param <TR>
     * @return
     */
    <TR> TR firstOrNull(Class<TR> resultClass, SqlExpression<SqlColumnAsSelector<T1, TR>> selectExpression);

    List<T1> toList();

    List<T1> toList(SqlExpression<SqlColumnSelector<T1>> selectExpression);

    <TR> List<TR> toList(Class<TR> resultClass);

    <TR> List<TR> toList(Class<TR> resultClass, SqlExpression<SqlColumnAsSelector<T1, TR>> selectExpression);

    //    <TR> List<TR> toList(Class<TR> resultClass);
    String toSql();

    String toSql(SqlExpression<SqlColumnSelector<T1>> selectExpression);

    <TR> String toSql(Class<TR> resultClass, SqlExpression<SqlColumnAsSelector<T1, TR>> selectExpression);

    String toSql(String columns);


    default TChain where(SqlExpression<SqlPredicate<T1>> whereExpression) {
        return where(true, whereExpression);
    }

    TChain where(boolean condition, SqlExpression<SqlPredicate<T1>> whereExpression);

    //    default TChain select(SqlExpression<SqlSelectColumnSelector<T1,TR>> selectExpression){
//        return select(true,selectExpression);
//    }
//    TChain select(boolean condition,SqlExpression<SqlSelectColumnSelector<T1,TR>> selectExpression);
    default TChain groupBy(SqlExpression<SqlColumnSelector<T1>> selectExpression) {
        return groupBy(true, selectExpression);
    }

    TChain groupBy(boolean condition, SqlExpression<SqlColumnSelector<T1>> selectExpression);

    default TChain having(SqlExpression<SqlAggregatePredicate<T1>> predicateExpression) {
        return having(true, predicateExpression);
    }

    TChain having(boolean condition, SqlExpression<SqlAggregatePredicate<T1>> predicateExpression);

    default TChain orderByAsc(SqlExpression<SqlColumnSelector<T1>> selectExpression) {
        return orderByAsc(true, selectExpression);
    }

    TChain orderByAsc(boolean condition, SqlExpression<SqlColumnSelector<T1>> selectExpression);

    default TChain orderByDesc(SqlExpression<SqlColumnSelector<T1>> selectExpression) {
        return orderByDesc(true, selectExpression);
    }

    TChain orderByDesc(boolean condition, SqlExpression<SqlColumnSelector<T1>> selectExpression);


    default TChain limit(long rows) {
        return limit(true, rows);
    }

    default TChain limit(boolean condition, long rows) {
        return limit(condition, 0, rows);
    }

    default TChain limit(long offset, long rows) {
        return limit(true, offset, rows);
    }

    TChain limit(boolean condition, long offset, long rows);
}
