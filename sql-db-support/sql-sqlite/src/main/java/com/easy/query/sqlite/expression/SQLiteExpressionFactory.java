package com.easy.query.sqlite.expression;

import com.easy.query.core.context.QueryRuntimeContext;
import com.easy.query.core.enums.MultiTableTypeEnum;
import com.easy.query.core.enums.SQLUnionEnum;
import com.easy.query.core.expression.parser.core.available.TableAvailable;
import com.easy.query.core.expression.sql.expression.AnonymousEntityQuerySQLExpression;
import com.easy.query.core.expression.sql.expression.EntityDeleteSQLExpression;
import com.easy.query.core.expression.sql.expression.EntityInsertSQLExpression;
import com.easy.query.core.expression.sql.expression.EntityQuerySQLExpression;
import com.easy.query.core.expression.sql.expression.EntityTableSQLExpression;
import com.easy.query.core.expression.sql.expression.EntityUpdateSQLExpression;
import com.easy.query.core.expression.sql.expression.factory.ExpressionFactory;
import com.easy.query.core.expression.sql.expression.impl.AnonymousEntityQuerySQLExpressionImpl;
import com.easy.query.core.expression.sql.expression.impl.AnonymousEntityTableSQLExpressionImpl;
import com.easy.query.core.expression.sql.expression.impl.AnonymousTreeCTEQuerySQLExpressionImpl;
import com.easy.query.core.expression.sql.expression.impl.AnonymousUnionQuerySQLExpressionImpl;
import com.easy.query.core.expression.sql.expression.impl.EntitySQLExpressionMetadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * create time 2023/8/20 21:41
 * 文件说明
 *
 * @author xuejiaming
 */
public class SQLiteExpressionFactory implements ExpressionFactory {
    @Override
    public EntityQuerySQLExpression createEasyQuerySQLExpression(EntitySQLExpressionMetadata entitySQLExpressionMetadata) {
        return new SQLiteQuerySQLExpression(entitySQLExpressionMetadata);
    }

    @Override
    public EntityInsertSQLExpression createEasyInsertSQLExpression(EntitySQLExpressionMetadata entitySQLExpressionMetadata, EntityTableSQLExpression entityTableSQLExpression) {
        return new SQLiteInsertSQLExpression(entitySQLExpressionMetadata, entityTableSQLExpression);
    }

    @Override
    public EntityUpdateSQLExpression createEasyUpdateSQLExpression(EntitySQLExpressionMetadata entitySQLExpressionMetadata) {
        return new SQLiteUpdateSQLExpression(entitySQLExpressionMetadata);
    }

    @Override
    public EntityDeleteSQLExpression createEasyDeleteSQLExpression(EntitySQLExpressionMetadata entitySQLExpressionMetadata, EntityTableSQLExpression entityTableSQLExpression) {
        return new SQLiteDeleteSQLExpression(entitySQLExpressionMetadata, entityTableSQLExpression);
    }

    @Override
    public EntityTableSQLExpression createEntityTableSQLExpression(TableAvailable entityTable, MultiTableTypeEnum multiTableType, QueryRuntimeContext runtimeContext) {
        return new SQLiteTableSQLExpression(entityTable, multiTableType, runtimeContext);
    }

    @Override
    public EntityTableSQLExpression createAnonymousEntityTableSQLExpression(TableAvailable entityTable, MultiTableTypeEnum multiTableType, EntityQuerySQLExpression entityQuerySQLExpression, QueryRuntimeContext runtimeContext) {
        return new AnonymousEntityTableSQLExpressionImpl(entityTable, multiTableType, entityQuerySQLExpression, runtimeContext);
    }


    @Override
    public AnonymousEntityQuerySQLExpression createEasyAnonymousQuerySQLExpression(EntitySQLExpressionMetadata entitySQLExpressionMetadata, String sql, Collection<Object> sqlParams) {
        return new AnonymousEntityQuerySQLExpressionImpl(entitySQLExpressionMetadata, sql, sqlParams);
    }

    @Override
    public AnonymousEntityQuerySQLExpression createEasyAnonymousUnionQuerySQLExpression(EntitySQLExpressionMetadata entitySQLExpressionMetadata, List<EntityQuerySQLExpression> entityQuerySQLExpressions, SQLUnionEnum sqlUnion) {
        return new AnonymousUnionQuerySQLExpressionImpl(entitySQLExpressionMetadata, new ArrayList<>(entityQuerySQLExpressions), sqlUnion);
    }

    @Override
    public AnonymousEntityQuerySQLExpression createEasyAnonymousCTEQuerySQLExpression(String cteTableName, EntitySQLExpressionMetadata entitySQLExpressionMetadata, EntityQuerySQLExpression querySQLExpression) {
        return new AnonymousTreeCTEQuerySQLExpressionImpl(cteTableName,entitySQLExpressionMetadata,querySQLExpression);
    }
}