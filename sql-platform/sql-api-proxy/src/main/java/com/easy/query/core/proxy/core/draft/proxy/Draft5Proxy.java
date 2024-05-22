package com.easy.query.core.proxy.core.draft.proxy;

import com.easy.query.core.proxy.SQLSelectAsExpression;
import com.easy.query.core.proxy.columns.SQLAnyColumn;
import com.easy.query.core.proxy.core.draft.Draft5;
import com.easy.query.core.proxy.fetcher.AbstractFetcher;
import com.easy.query.core.util.EasyObjectUtil;

import java.util.Optional;

/**
 * this file automatically generated by easy-query, don't modify it
 * 当前文件是easy-query自动生成的请不要随意修改
 *
 * @author xuejiaming
 */
public class Draft5Proxy<T1,T2,T3,T4,T5> extends AbstractDraftProxy<Draft5Proxy<T1,T2,T3,T4,T5>, Draft5<T1,T2,T3,T4,T5>> {

    private static final Class<Draft5> entityClass = Draft5.class;

    public static <TR1,TR2,TR3,TR4,TR5> Draft5Proxy<TR1,TR2,TR3,TR4,TR5> createTable() {
        return new Draft5Proxy<>();
    }

    public Draft5Proxy() {
        super(5);
    }

    /**
     * {@link Draft5#getValue1}
     */
    public SQLAnyColumn<Draft5Proxy<T1,T2,T3,T4,T5>, T1> value1() {
        return getAnyColumn("value1",EasyObjectUtil.typeCastNullable(Optional.ofNullable(getDraftPropTypes()[0]).map(o->o.getPropertyType()).orElse(null)));
    }

    /**
     * {@link Draft5#getValue2()}
     */
    public SQLAnyColumn<Draft5Proxy<T1,T2,T3,T4,T5>, T2> value2() {
        return getAnyColumn("value2",EasyObjectUtil.typeCastNullable(Optional.ofNullable(getDraftPropTypes()[1]).map(o->o.getPropertyType()).orElse(null)));
    }
    /**
     * {@link Draft5#getValue3()}
     */
    public SQLAnyColumn<Draft5Proxy<T1,T2,T3,T4,T5>, T3> value3() {
        return getAnyColumn("value3",EasyObjectUtil.typeCastNullable(Optional.ofNullable(getDraftPropTypes()[2]).map(o->o.getPropertyType()).orElse(null)));
    }
    /**
     * {@link Draft5#getValue4()}
     */
    public SQLAnyColumn<Draft5Proxy<T1,T2,T3,T4,T5>, T4> value4() {
        return getAnyColumn("value4",EasyObjectUtil.typeCastNullable(Optional.ofNullable(getDraftPropTypes()[3]).map(o->o.getPropertyType()).orElse(null)));
    }
    /**
     * {@link Draft5#getValue5()}
     */
    public SQLAnyColumn<Draft5Proxy<T1,T2,T3,T4,T5>, T5> value5() {
        return getAnyColumn("value5",EasyObjectUtil.typeCastNullable(Optional.ofNullable(getDraftPropTypes()[4]).map(o->o.getPropertyType()).orElse(null)));
    }


    @Override
    public Class<Draft5<T1,T2,T3,T4,T5>> getEntityClass() {
        return EasyObjectUtil.typeCastNullable(entityClass);
    }


    /**
     * 数据库列的简单获取
     *
     * @return
     */
    public Draft5ProxyFetcher<T1,T2,T3,T4,T5> FETCHER = new Draft5ProxyFetcher<>(this, null, SQLSelectAsExpression.empty);


    public static class Draft5ProxyFetcher<TF1,TF2,TF3,TF4,TF5> extends AbstractFetcher<Draft5Proxy<TF1,TF2,TF3,TF4,TF5>, Draft5<TF1,TF2,TF3,TF4,TF5>, Draft5ProxyFetcher<TF1,TF2,TF3,TF4,TF5>> {

        public Draft5ProxyFetcher(Draft5Proxy<TF1,TF2,TF3,TF4,TF5> proxy, Draft5ProxyFetcher<TF1,TF2,TF3,TF4,TF5> prev, SQLSelectAsExpression sqlSelectAsExpression) {
            super(proxy, prev, sqlSelectAsExpression);
        }


        /**
         * {@link Draft5#getValue1}
         */
        public Draft5ProxyFetcher<TF1,TF2,TF3,TF4,TF5> value1() {
            return add(getProxy().value1());
        }


        /**
         * {@link Draft5#getValue2}
         */
        public Draft5ProxyFetcher<TF1,TF2,TF3,TF4,TF5> value2() {
            return add(getProxy().value2());
        }
        /**
         * {@link Draft5#getValue3}
         */
        public Draft5ProxyFetcher<TF1,TF2,TF3,TF4,TF5> value3() {
            return add(getProxy().value3());
        }
        /**
         * {@link Draft5#getValue4}
         */
        public Draft5ProxyFetcher<TF1,TF2,TF3,TF4,TF5> value4() {
            return add(getProxy().value4());
        }
        /**
         * {@link Draft5#getValue5}
         */
        public Draft5ProxyFetcher<TF1,TF2,TF3,TF4,TF5> value5() {
            return add(getProxy().value5());
        }


        @Override
        protected Draft5ProxyFetcher<TF1,TF2,TF3,TF4,TF5> createFetcher(
                Draft5Proxy<TF1,TF2,TF3,TF4,TF5> cp,
                AbstractFetcher<Draft5Proxy<TF1,TF2,TF3,TF4,TF5>, Draft5<TF1,TF2,TF3,TF4,TF5>, Draft5ProxyFetcher<TF1,TF2,TF3,TF4,TF5>> prev,
                SQLSelectAsExpression sqlSelectExpression
        ) {
            return new Draft5ProxyFetcher<>(cp, this, sqlSelectExpression);
        }
    }

}
