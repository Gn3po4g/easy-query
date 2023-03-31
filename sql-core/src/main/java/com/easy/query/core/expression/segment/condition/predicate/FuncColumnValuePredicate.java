package com.easy.query.core.expression.segment.condition.predicate;

import com.easy.query.core.enums.EasyFunc;
import com.easy.query.core.expression.sql.EntityExpression;
import com.easy.query.core.expression.sql.EntityTableExpression;
import com.easy.query.core.basic.jdbc.parameter.EasyConstSQLParameter;
import com.easy.query.core.enums.SqlPredicateCompare;

/**
 * @FileName: FuncColumnValuePredicate.java
 * @Description: column加方法函数和对应的值比较
 * @Date: 2023/2/14 23:34
 * @author xuejiaming
 */
public class FuncColumnValuePredicate implements Predicate {
    private final EntityTableExpression table;
    private final EasyFunc func;
    private final String propertyName;
    private final Object val;
    private final SqlPredicateCompare compare;
    private final EntityExpression sqlEntityExpression;

    public FuncColumnValuePredicate(EntityTableExpression table, EasyFunc func, String propertyName, Object val, SqlPredicateCompare compare, EntityExpression sqlEntityExpression) {
        this.table = table;
        this.propertyName = propertyName;

        this.func = func;
        this.val = val;
        this.compare = compare;
        this.sqlEntityExpression = sqlEntityExpression;
    }

    @Override
    public String toSql() {
        sqlEntityExpression.addParameter(new EasyConstSQLParameter(table,propertyName,val));
        String sqlColumnSegment = sqlEntityExpression.getSqlOwnerColumn(table,propertyName);
        return func.getFuncColumn(sqlColumnSegment) +" "+ compare.getSql() + " ?";
    }

    @Override
    public EntityTableExpression getTable() {
        return table;
    }

    @Override
    public String getPropertyName() {
        return propertyName;
    }
}
