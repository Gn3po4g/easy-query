package com.easy.query.clickhouse.func;

import com.easy.query.core.expression.parser.core.available.TableAvailable;
import com.easy.query.core.func.column.ColumnExpression;
import com.easy.query.core.func.column.impl.ColumnFuncValueExpressionImpl;
import com.easy.query.core.func.def.AbstractExpressionSQLFunction;
import com.easy.query.core.util.EasyCollectionUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * create time 2023/12/21 11:58
 * 文件说明
 *
 * @author xuejiaming
 */
public class ClickHouseDateTimePlusSQLFunction extends AbstractExpressionSQLFunction {
    private final List<ColumnExpression> columnExpressions;
    private final long duration;
    private final TimeUnit timeUnit;

    public ClickHouseDateTimePlusSQLFunction(List<ColumnExpression> columnExpressions, long duration, TimeUnit timeUnit) {

        this.columnExpressions = columnExpressions;
        this.duration = duration;
        this.timeUnit = timeUnit;
    }

    @Override
    public String sqlSegment(TableAvailable defaultTable) {
        if(EasyCollectionUtil.isEmpty(columnExpressions)){
            throw new IllegalArgumentException("columnExpressions is empty");
        }
        ColumnExpression columnExpression = columnExpressions.get(0);
        columnExpressions.clear();
        columnExpressions.add(columnExpression);
        long micros = timeUnit.toMicros(duration);
        columnExpressions.add(new ColumnFuncValueExpressionImpl(micros));
        return "addSeconds(toDateTime({0}),{1})";
    }

    @Override
    public int paramMarks() {
        return columnExpressions.size();
    }

    @Override
    protected List<ColumnExpression> getColumnExpressions() {
        return columnExpressions;
    }

}
