package com.easy.query.core.basic.jdbc.parameter;

import com.easy.query.core.expression.sql.SqlEntityTableExpression;
import com.easy.query.core.expression.sql.SqlTableExpressionSegment;

/**
 * @FileName: ConstSqlParameter.java
 * @Description: 文件说明
 * @Date: 2023/2/28 20:47
 * @author xuejiaming
 */
public final class ConstLikeSQLParameter implements ConstSQLParameter, SQLLikeParameter {
    private final EasyConstSQLParameter constSQLParameter;

    public ConstLikeSQLParameter(EasyConstSQLParameter constSQLParameter) {
        this.constSQLParameter = constSQLParameter;
    }

    @Override
    public SqlEntityTableExpression getTable() {
        return constSQLParameter.getTable();
    }

    @Override
    public String getPropertyName() {
        return constSQLParameter.getPropertyName();
    }

    @Override
    public Object getValue() {
        return constSQLParameter.getValue();
    }
}
