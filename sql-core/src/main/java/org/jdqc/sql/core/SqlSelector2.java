package org.jdqc.sql.core;

/**
 * Copyright (c) 2021.biaodian.All Rights Reserved
 *
 * @FileName: Selector.java
 * @Description: 文件说明
 * @Date: 2023/2/6 04:42
 * @Created by xuejiaming
 */
public interface SqlSelector2<Children,T1,TR> extends SqlSelector<Children,T1,TR> {
    Children selectAll(Class<?> selectClass);
   <TSelect> Children selectAs(Property<TSelect,?> column,Property<TR, ?> alias);
//    Select<T1,TR> selectCount(Property<T1,?> column,Property<TR,?> alias);
//    Select<T1,TR> selectSum(Property<T1,?> column,Property<TR,?> alias);
//    Select<T1,TR> selectMax(Property<T1,?> column,Property<TR,?> alias);
//    Select<T1,TR> selectMin(Property<T1,?> column,Property<TR,?> alias);
//    Select<T1,TR> selectAvg(Property<T1,?> column,Property<TR,?> alias);
//    Select<T1,TR> selectLen(Property<T1,?> column,Property<TR,?> alias);
}
