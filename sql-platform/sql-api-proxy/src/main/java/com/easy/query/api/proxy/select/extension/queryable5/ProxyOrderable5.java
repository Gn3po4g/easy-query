package com.easy.query.api.proxy.select.extension.queryable5;

import com.easy.query.api.proxy.select.ProxyQueryable5;
import com.easy.query.api.proxy.select.extension.queryable5.sql.MultiProxyOrderSelector5;
import com.easy.query.api.proxy.select.extension.queryable5.sql.impl.MultiProxyOrderSelector5Impl;
import com.easy.query.core.expression.lambda.SQLExpression1;
import com.easy.query.core.proxy.ProxyEntity;

/**
 * create time 2023/8/16 08:50
 * 文件说明
 *
 * @author xuejiaming
 */
public interface ProxyOrderable5<T1Proxy extends ProxyEntity<T1Proxy, T1>, T1,
        T2Proxy extends ProxyEntity<T2Proxy, T2>, T2,
        T3Proxy extends ProxyEntity<T3Proxy, T3>, T3,
        T4Proxy extends ProxyEntity<T4Proxy, T4>, T4,
        T5Proxy extends ProxyEntity<T5Proxy, T5>, T5> extends ClientProxyQueryable5Available<T1, T2, T3, T4, T5>, ProxyQueryable5Available<T1Proxy, T1, T2Proxy, T2, T3Proxy, T3, T4Proxy, T4, T5Proxy, T5> {

    default ProxyQueryable5<T1Proxy, T1, T2Proxy, T2, T3Proxy, T3, T4Proxy, T4, T5Proxy, T5> orderByAsc(SQLExpression1<MultiProxyOrderSelector5<T1Proxy, T2Proxy, T3Proxy, T4Proxy, T5Proxy>> selectExpression) {
        return orderByAsc(true, selectExpression);
    }

    default ProxyQueryable5<T1Proxy, T1, T2Proxy, T2, T3Proxy, T3, T4Proxy, T4, T5Proxy, T5> orderByAsc(boolean condition, SQLExpression1<MultiProxyOrderSelector5<T1Proxy, T2Proxy, T3Proxy, T4Proxy, T5Proxy>> selectExpression) {
        if (condition) {
            getClientQueryable5().orderByAsc((t, t1, t2, t3, t4) -> {
                selectExpression.apply(new MultiProxyOrderSelector5Impl<>(t.getOrderSelector(), get1Proxy(), get2Proxy(), get3Proxy(), get4Proxy(), get5Proxy()));
            });
        }
        return getQueryable5();
    }

    default ProxyQueryable5<T1Proxy, T1, T2Proxy, T2, T3Proxy, T3, T4Proxy, T4, T5Proxy, T5> orderByDesc(SQLExpression1<MultiProxyOrderSelector5<T1Proxy, T2Proxy, T3Proxy, T4Proxy, T5Proxy>> selectExpression) {
        return orderByDesc(true, selectExpression);
    }

    default ProxyQueryable5<T1Proxy, T1, T2Proxy, T2, T3Proxy, T3, T4Proxy, T4, T5Proxy, T5> orderByDesc(boolean condition, SQLExpression1<MultiProxyOrderSelector5<T1Proxy, T2Proxy, T3Proxy, T4Proxy, T5Proxy>> selectExpression) {
        if (condition) {
            getClientQueryable5().orderByDesc((t, t1, t2, t3,t4) -> {
                selectExpression.apply(new MultiProxyOrderSelector5Impl<>(t.getOrderSelector(), get1Proxy(), get2Proxy(), get3Proxy(), get4Proxy(), get5Proxy()));
            });
        }
        return getQueryable5();
    }

}
