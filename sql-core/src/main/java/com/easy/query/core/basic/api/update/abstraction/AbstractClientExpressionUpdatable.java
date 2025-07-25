package com.easy.query.core.basic.api.update.abstraction;

import com.easy.query.core.basic.api.internal.AbstractSQLExecuteRows;
import com.easy.query.core.basic.api.update.ClientExpressionUpdatable;
import com.easy.query.core.basic.jdbc.executor.EntityExpressionExecutor;
import com.easy.query.core.basic.jdbc.executor.EntityExpressionPrepareExecutor;
import com.easy.query.core.basic.jdbc.executor.ExecutorContext;
import com.easy.query.core.basic.jdbc.parameter.ToSQLContext;
import com.easy.query.core.context.QueryRuntimeContext;
import com.easy.query.core.enums.EasyBehaviorEnum;
import com.easy.query.core.enums.ExecuteMethodEnum;
import com.easy.query.core.enums.MultiTableTypeEnum;
import com.easy.query.core.enums.SQLPredicateCompareEnum;
import com.easy.query.core.expression.builder.impl.FilterImpl;
import com.easy.query.core.expression.lambda.SQLActionExpression1;
import com.easy.query.core.expression.parser.core.available.TableAvailable;
import com.easy.query.core.expression.parser.core.base.ColumnSetter;
import com.easy.query.core.expression.parser.core.base.WherePredicate;
import com.easy.query.core.expression.parser.core.base.core.FilterContext;
import com.easy.query.core.expression.parser.core.base.impl.ColumnSetterImpl;
import com.easy.query.core.expression.parser.core.base.impl.WherePredicateImpl;
import com.easy.query.core.expression.parser.core.base.scec.SQLNativePropertyExpressionContext;
import com.easy.query.core.expression.segment.Column2Segment;
import com.easy.query.core.expression.segment.ColumnValue2Segment;
import com.easy.query.core.expression.segment.condition.AndPredicateSegment;
import com.easy.query.core.expression.segment.condition.PredicateSegment;
import com.easy.query.core.expression.segment.condition.predicate.ColumnCollectionPredicate;
import com.easy.query.core.expression.segment.condition.predicate.ColumnValuePredicate;
import com.easy.query.core.expression.sql.builder.EntityTableExpressionBuilder;
import com.easy.query.core.expression.sql.builder.EntityUpdateExpressionBuilder;
import com.easy.query.core.expression.sql.builder.ExpressionContext;
import com.easy.query.core.expression.sql.builder.internal.ContextConfigurer;
import com.easy.query.core.expression.sql.builder.internal.ContextConfigurerImpl;
import com.easy.query.core.metadata.ColumnMetadata;
import com.easy.query.core.metadata.EntityMetadata;
import com.easy.query.core.util.EasyCollectionUtil;
import com.easy.query.core.util.EasyColumnSegmentUtil;
import com.easy.query.core.util.EasySQLExpressionUtil;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author xuejiaming
 * @FileName: AbstractExpressionUpdate.java
 * @Description: 文件说明
 * create time 2023/2/25 08:24
 */
public abstract class AbstractClientExpressionUpdatable<T> extends AbstractSQLExecuteRows<ClientExpressionUpdatable<T>> implements ClientExpressionUpdatable<T> {
    protected final Class<T> clazz;
    protected final EntityMetadata entityMetadata;
    protected final EntityUpdateExpressionBuilder entityUpdateExpressionBuilder;
    protected final ExpressionContext expressionContext;
    protected final ColumnSetter<T> columnSetter;
    protected final TableAvailable table;

    public AbstractClientExpressionUpdatable(Class<T> clazz, EntityUpdateExpressionBuilder entityUpdateExpressionBuilder) {
        super(entityUpdateExpressionBuilder);

        this.clazz = clazz;
        this.entityUpdateExpressionBuilder = entityUpdateExpressionBuilder;
        this.expressionContext = entityUpdateExpressionBuilder.getExpressionContext();
        QueryRuntimeContext runtimeContext = entityUpdateExpressionBuilder.getRuntimeContext();
        entityMetadata = runtimeContext.getEntityMetadataManager().getEntityMetadata(clazz);
        entityMetadata.checkTable();
        EntityTableExpressionBuilder table = runtimeContext.getExpressionBuilderFactory().createEntityTableExpressionBuilder(entityMetadata, MultiTableTypeEnum.NONE, expressionContext);
        this.table = table.getEntityTable();
        this.entityUpdateExpressionBuilder.addSQLEntityTableExpression(table);
        columnSetter = new ColumnSetterImpl<>(this.table, entityUpdateExpressionBuilder, entityUpdateExpressionBuilder.getSetColumns());
    }

    @Override
    public ColumnSetter<T> getColumnSetter() {
        return columnSetter;
    }

    @Override
    public EntityUpdateExpressionBuilder getUpdateExpressionBuilder() {
        return entityUpdateExpressionBuilder;
    }

    @Override
    public long executeRows() {
        QueryRuntimeContext runtimeContext = entityUpdateExpressionBuilder.getRuntimeContext();
        EntityExpressionPrepareExecutor entityExpressionPrepareExecutor = runtimeContext.getEntityExpressionPrepareExecutor();
        return entityExpressionPrepareExecutor.executeRows(ExecutorContext.create(entityUpdateExpressionBuilder.getExpressionContext(), false, ExecuteMethodEnum.UPDATE), entityUpdateExpressionBuilder, entityUpdateExpressionBuilder.toExpression());
    }

    @Override
    public ClientExpressionUpdatable<T> set(boolean condition, String property, Object val) {
        columnSetter.set(condition, property, val);
        return this;
    }

    @Override
    public ClientExpressionUpdatable<T> setWithColumn(boolean condition, String property1, String property2) {
        columnSetter.setWithColumn(condition, property1, property2);
        return this;
    }

    @Override
    public ClientExpressionUpdatable<T> withVersion(boolean condition, Object versionValue) {
        if (condition) {
            entityUpdateExpressionBuilder.getExpressionContext().setVersion(versionValue);
        }
        return this;
    }

    @Override
    public ClientExpressionUpdatable<T> setIncrementNumber(boolean condition, String property, Number val) {
        columnSetter.setIncrementNumber(condition, property, val);
        return this;
    }

    @Override
    public ClientExpressionUpdatable<T> setDecrementNumber(boolean condition, String property, Number val) {
        columnSetter.setDecrementNumber(condition, property, val);
        return this;
    }

    @Override
    public ClientExpressionUpdatable<T> setSQLSegment(boolean condition, String property, String sqlSegment, SQLActionExpression1<SQLNativePropertyExpressionContext> contextConsume) {
        if (condition) {
            columnSetter.setSQLSegment(property, sqlSegment, contextConsume);
        }
        return this;
    }

    @Override
    public ClientExpressionUpdatable<T> where(boolean condition, SQLActionExpression1<WherePredicate<T>> whereExpression) {
        if (condition) {
            FilterImpl filter = new FilterImpl(entityUpdateExpressionBuilder.getRuntimeContext(), entityUpdateExpressionBuilder.getExpressionContext(), entityUpdateExpressionBuilder.getWhere(), false, entityUpdateExpressionBuilder.getExpressionContext().getValueFilter());
            WherePredicateImpl<T> sqlPredicate = new WherePredicateImpl<>(table, new FilterContext(filter, entityUpdateExpressionBuilder));
            whereExpression.apply(sqlPredicate);
        }
        return this;
    }

    @Override
    public ClientExpressionUpdatable<T> whereById(boolean condition, Object id) {

        if (condition) {

            PredicateSegment where = entityUpdateExpressionBuilder.getWhere();

            String keyProperty = EasySQLExpressionUtil.getSingleKeyPropertyName(table);
            AndPredicateSegment andPredicateSegment = new AndPredicateSegment();
            ColumnMetadata columnMetadata = table.getEntityMetadata().getColumnNotNull(keyProperty);
            Column2Segment column2Segment = EasyColumnSegmentUtil.createColumn2Segment(table, columnMetadata, entityUpdateExpressionBuilder.getExpressionContext());
            ColumnValue2Segment compareValue2Segment = EasyColumnSegmentUtil.createColumnCompareValue2Segment(table, columnMetadata, entityUpdateExpressionBuilder.getExpressionContext(), id, SQLPredicateCompareEnum.EQ.isLike());

            andPredicateSegment
                    .setPredicate(new ColumnValuePredicate(column2Segment, compareValue2Segment, SQLPredicateCompareEnum.EQ));
            where.addPredicateSegment(andPredicateSegment);
        }
        return this;
    }

    @Override
    public <TProperty> ClientExpressionUpdatable<T> whereByIds(boolean condition, Collection<TProperty> ids) {

        if (condition) {
            String keyProperty = EasySQLExpressionUtil.getSingleKeyPropertyName(table);
            AndPredicateSegment andPredicateSegment = new AndPredicateSegment();
            PredicateSegment where = entityUpdateExpressionBuilder.getWhere();

            ColumnMetadata columnMetadata = table.getEntityMetadata().getColumnNotNull(keyProperty);
            Column2Segment column2Segment = EasyColumnSegmentUtil.createColumn2Segment(table, columnMetadata, expressionContext);
//            List<ColumnValue2Segment> columnValue2Segments = ids.stream().map(o -> EasyColumnSegmentUtil.createColumnCompareValue2Segment(table, columnMetadata, expressionContext, o)).collect(Collectors.toList());

            List<ColumnValue2Segment> columnValue2Segments = EasyCollectionUtil.select(ids, (o, i) -> EasyColumnSegmentUtil.createColumnCompareValue2Segment(table, columnMetadata, expressionContext, o));
            andPredicateSegment
                    .setPredicate(new ColumnCollectionPredicate(column2Segment, columnValue2Segments, SQLPredicateCompareEnum.IN, entityUpdateExpressionBuilder.getExpressionContext()));
            where.addPredicateSegment(andPredicateSegment);
        }
        return this;
    }

    @Override
    public ExpressionContext getExpressionContext() {
        return entityUpdateExpressionBuilder.getExpressionContext();
    }

    @Override
    public ClientExpressionUpdatable<T> asTable(Function<String, String> tableNameAs) {
        entityUpdateExpressionBuilder.getRecentlyTable().setTableNameAs(tableNameAs);
        return this;
    }

    @Override
    public ClientExpressionUpdatable<T> asSchema(Function<String, String> schemaAs) {
        entityUpdateExpressionBuilder.getRecentlyTable().setSchemaAs(schemaAs);
        return this;
    }

    @Override
    public ClientExpressionUpdatable<T> asAlias(String alias) {
        entityUpdateExpressionBuilder.getRecentlyTable().asAlias(alias);
        return this;
    }

    @Override
    public ClientExpressionUpdatable<T> asTableLink(Function<String, String> linkAs) {
        entityUpdateExpressionBuilder.getRecentlyTable().setTableLinkAs(linkAs);
        return this;
    }

    @Override
    public ClientExpressionUpdatable<T> asTableSegment(BiFunction<String, String, String> segmentAs) {
        entityUpdateExpressionBuilder.getRecentlyTable().setTableSegmentAs(segmentAs);
        return this;
    }

    @Override
    public ClientExpressionUpdatable<T> configure(SQLActionExpression1<ContextConfigurer> configurer) {
        if (configurer != null) {
            configurer.apply(new ContextConfigurerImpl(entityUpdateExpressionBuilder.getExpressionContext()));
        }
        return this;
    }

    @Override
    public ClientExpressionUpdatable<T> ignoreVersion(boolean ignored) {

        if (ignored) {
            entityUpdateExpressionBuilder.getExpressionContext().getBehavior().addBehavior(EasyBehaviorEnum.IGNORE_VERSION);
        } else {
            entityUpdateExpressionBuilder.getExpressionContext().getBehavior().removeBehavior(EasyBehaviorEnum.IGNORE_VERSION);
        }
        return this;
    }

    @Override
    public String toSQL(ToSQLContext toSQLContext) {
        return entityUpdateExpressionBuilder.toExpression().toSQL(toSQLContext);
    }
}
