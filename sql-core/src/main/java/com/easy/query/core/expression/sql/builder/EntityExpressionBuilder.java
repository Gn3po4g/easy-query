package com.easy.query.core.expression.sql.builder;

import com.easy.query.core.expression.parser.core.available.RuntimeContextAvailable;
import com.easy.query.core.exception.EasyQueryInvalidOperationException;
import com.easy.query.core.expression.RelationTableKey;
import com.easy.query.core.expression.sql.expression.EntitySQLExpression;
import com.easy.query.core.expression.visitor.TableVisitor;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @Description: 文件说明
 * @Date: 2023/3/4 16:29
 * @author xuejiaming
 */
public interface EntityExpressionBuilder extends ExpressionBuilder , RuntimeContextAvailable {
    default boolean isQuery(){
        return false;
    }
    Class<?> getQueryClass();
    ExpressionContext getExpressionContext();

    /**
     * 添加表
     * @param tableExpression
     */
    void addSQLEntityTableExpression(EntityTableExpressionBuilder tableExpression);
    EntityTableExpressionBuilder addRelationEntityTableExpression(RelationTableKey relationTableKey, Function<RelationTableKey,EntityTableExpressionBuilder> tableExpressionSupplier);
//    EntityTableExpressionBuilder removeRelationEntityTableExpression(RelationTableKey relationTableKey);
    Map<RelationTableKey,EntityTableExpressionBuilder> getRelationTables();
    boolean hasRelationTables();
    List<EntityTableExpressionBuilder> getTables();
    default EntityTableExpressionBuilder getTable(int index){
        return getTables().get(index);
    }
    default EntityTableExpressionBuilder getRecentlyTable(){
        int size = getTables().size();
        if(size==0){
            throw new EasyQueryInvalidOperationException("cant get recently table");
        }
        return getTable(size-1);
    }
    void setLogicDelete(boolean logicDelete);

    @Override
    EntitySQLExpression toExpression();

    EntityExpressionBuilder cloneEntityExpressionBuilder();
    default void accept(TableVisitor visitor){

    }
}
