package com.easy.query.core.expression.parser.impl;

import com.easy.query.core.expression.segment.SqlEntitySegment;
import com.easy.query.core.expression.segment.builder.SqlBuilderSegment;
import com.easy.query.core.expression.lambda.Property;
import com.easy.query.core.expression.parser.abstraction.SqlColumnSelector;
import com.easy.query.core.expression.segment.condition.predicate.ColumnPropertyPredicate;
import com.easy.query.core.expression.sql.builder.EntityExpressionBuilder;
import com.easy.query.core.expression.sql.builder.EntityTableExpressionBuilder;

import java.util.Collection;
import java.util.Objects;

/**
 * @FileName: DefaultSqlColumnSetSelector.java
 * @Description: 文件说明
 * @Date: 2023/2/25 21:31
 * @author xuejiaming
 */
public class DefaultSqlColumnSetSelector<T> implements SqlColumnSelector<T> {
    private final int index;
    private final EntityExpressionBuilder sqlEntityExpression;
    private final SqlBuilderSegment sqlSegmentBuilder;

    public DefaultSqlColumnSetSelector(int index, EntityExpressionBuilder sqlEntityExpression, SqlBuilderSegment sqlSegmentBuilder){

        this.index = index;
        this.sqlEntityExpression = sqlEntityExpression;
        this.sqlSegmentBuilder = sqlSegmentBuilder;
    }
    @Override
    public SqlColumnSelector<T> column(Property<T, ?> column) {
        EntityTableExpressionBuilder table = sqlEntityExpression.getTable(index);
        String propertyName = table.getPropertyName(column);
        sqlSegmentBuilder.append(new ColumnPropertyPredicate(table.getEntityTable(),propertyName,sqlEntityExpression.getRuntimeContext()));
        return this;
    }

    @Override
    public SqlColumnSelector<T> columnIgnore(Property<T, ?> column) {

        EntityTableExpressionBuilder table = sqlEntityExpression.getTable(index);
        String propertyName = table.getPropertyName(column);
        sqlSegmentBuilder.getSqlSegments().removeIf(sqlSegment -> {
            if (sqlSegment instanceof SqlEntitySegment) {
                SqlEntitySegment sqlEntitySegment = (SqlEntitySegment) sqlSegment;
                return Objects.equals(sqlEntitySegment.getTable(), table) && Objects.equals(sqlEntitySegment.getPropertyName(), propertyName);
            }
            return false;
        });
        return this;
    }

    @Override
    public SqlColumnSelector<T> columnAll() {
        EntityTableExpressionBuilder table = sqlEntityExpression.getTable(index);
        Collection<String> properties = table.getEntityMetadata().getProperties();
        for (String property : properties) {
            sqlSegmentBuilder.append(new ColumnPropertyPredicate(table.getEntityTable(), property,sqlEntityExpression.getRuntimeContext()));
        }
        return this;
    }

    @Override
    public int getIndex() {
        return index;
    }
}
