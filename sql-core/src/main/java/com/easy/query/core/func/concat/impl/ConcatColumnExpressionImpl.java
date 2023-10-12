package com.easy.query.core.func.concat.impl;

import com.easy.query.core.expression.parser.core.available.TableAvailable;
import com.easy.query.core.func.concat.ConcatColumnExpression;

/**
 * create time 2023/10/12 14:18
 * 文件说明
 *
 * @author xuejiaming
 */
public class ConcatColumnExpressionImpl implements ConcatColumnExpression {
    private final TableAvailable table;
    private final String property;

    public ConcatColumnExpressionImpl(TableAvailable table, String property){
        this.table = table;

        this.property = property;
    }
    @Override
    public TableAvailable getTableOrNull() {
        return table;
    }

    @Override
    public String getProperty() {
        return property;
    }
}
