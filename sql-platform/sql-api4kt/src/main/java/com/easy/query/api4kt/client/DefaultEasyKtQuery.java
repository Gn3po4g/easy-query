package com.easy.query.api4kt.client;

import com.easy.query.api4kt.delete.KtEntityDeletable;
import com.easy.query.api4kt.delete.KtExpressionDeletable;
import com.easy.query.api4kt.delete.impl.EasyKtEntityDeletable;
import com.easy.query.api4kt.delete.impl.EasyKtExpressionDeletable;
import com.easy.query.api4kt.func.DefaultSQLKtLambdaFunc;
import com.easy.query.api4kt.func.SQLKtLambdaFunc;
import com.easy.query.api4kt.insert.EasyKtEntityInsertable;
import com.easy.query.api4kt.insert.KtEntityInsertable;
import com.easy.query.api4kt.select.KtQueryable;
import com.easy.query.api4kt.select.impl.EasyKtQueryable;
import com.easy.query.api4kt.update.KtEntityUpdatable;
import com.easy.query.api4kt.update.KtExpressionUpdatable;
import com.easy.query.api4kt.update.impl.EasyKtEntityUpdatable;
import com.easy.query.api4kt.update.impl.EasyKtExpressionUpdatable;
import com.easy.query.core.api.client.EasyQueryClient;
import com.easy.query.core.basic.extension.track.EntityState;
import com.easy.query.core.basic.jdbc.tx.Transaction;
import com.easy.query.core.context.QueryRuntimeContext;

import java.util.Collection;

/**
 * @author xuejiaming
 * @Date: 2023/3/6 13:30
 */
public class DefaultEasyKtQuery implements EasyKtQuery {
    private final EasyQueryClient easyQueryClient;
    private final SQLKtLambdaFunc sqlKtLambdaFunc;

    public DefaultEasyKtQuery(EasyQueryClient easyQueryClient) {
        this.easyQueryClient = easyQueryClient;
        this.sqlKtLambdaFunc = new DefaultSQLKtLambdaFunc(easyQueryClient.sqlFunc());
    }

    @Override
    public EasyQueryClient getEasyQueryClient() {
        return easyQueryClient;
    }

    @Override
    public QueryRuntimeContext getRuntimeContext() {
        return easyQueryClient.getRuntimeContext();
    }


    @Override
    public <T> KtQueryable<T> queryable(Class<T> clazz) {
        return new EasyKtQueryable<>(easyQueryClient.queryable(clazz));
    }

    @Override
    public <T> KtQueryable<T> queryable(String sql, Class<T> clazz, Collection<Object> sqlParams) {
        return new EasyKtQueryable<>(easyQueryClient.queryable(sql, clazz, sqlParams));
    }

    @Override
    public Transaction beginTransaction(Integer isolationLevel) {
        return easyQueryClient.beginTransaction(isolationLevel);
    }

//    @Override
//    public <T> Insert<T> insert(Class<T> clazz) {
//        return new MySQLInsert<>(clazz,new InsertContext(runtimeContext));
//    }

    @Override
    public <T> KtEntityInsertable<T> insertable(T entity) {
        return new EasyKtEntityInsertable<>(easyQueryClient.insertable(entity));
    }

    @Override
    public <T> KtEntityInsertable<T> insertable(Collection<T> entities) {
        return new EasyKtEntityInsertable<>(easyQueryClient.insertable(entities));
    }

    @Override
    public <T> KtExpressionUpdatable<T> updatable(Class<T> entityClass) {
        return new EasyKtExpressionUpdatable<>(easyQueryClient.updatable(entityClass));
    }

    @Override
    public <T> KtEntityUpdatable<T> updatable(T entity) {
        return new EasyKtEntityUpdatable<>(easyQueryClient.updatable(entity));
    }

    @Override
    public <T> KtEntityUpdatable<T> updatable(Collection<T> entities) {
        return new EasyKtEntityUpdatable<>(easyQueryClient.updatable(entities));
    }

    @Override
    public <T> KtEntityDeletable<T> deletable(T entity) {
        return new EasyKtEntityDeletable<>(easyQueryClient.deletable(entity));
    }

    @Override
    public <T> KtEntityDeletable<T> deletable(Collection<T> entities) {
        return new EasyKtEntityDeletable<>(easyQueryClient.deletable(entities));
    }


    @Override
    public <T> KtExpressionDeletable<T> deletable(Class<T> entityClass) {
        return new EasyKtExpressionDeletable<>(easyQueryClient.deletable(entityClass));
    }

    @Override
    public boolean addTracking(Object entity) {
        return easyQueryClient.addTracking(entity);
    }

    @Override
    public boolean removeTracking(Object entity) {
        return easyQueryClient.removeTracking(entity);
    }

    @Override
    public EntityState getTrackEntityStateNotNull(Object entity) {
        return easyQueryClient.getTrackEntityStateNotNull(entity);
    }

    @Override
    public SQLKtLambdaFunc sqlFunc() {
        return sqlKtLambdaFunc;
    }
}
