package com.easy.query.test.entity.base.proxy;

import com.easy.query.core.proxy.AbstractProxyEntity;
import com.easy.query.core.proxy.SQLColumn;
import com.easy.query.core.proxy.SQLSelectAsExpression;
import com.easy.query.core.proxy.columns.SQLNavigateColumn;
import com.easy.query.core.proxy.fetcher.AbstractFetcher;
import com.easy.query.test.entity.base.City;

/**
 * this file automatically generated by easy-query, don't modify it
 * 当前文件是easy-query自动生成的请不要随意修改
 *
 * @author xuejiaming
 */
public class CityProxy extends AbstractProxyEntity<CityProxy, City> {

    private static final Class<City> entityClass = City.class;
    /**
     * 为entityQuery提供静态属性获取
     */
    public static final CityProxy TABLE = new CityProxy();

    public static CityProxy createTable() {
        return new CityProxy();
    }

    public CityProxy() {
    }

    /**
     * {@link City#getCode}
     */
    public SQLColumn<CityProxy, java.lang.String> code() {
        return get("code");
    }

    /**
     * {@link City#getProvinceCode}
     */
    public SQLColumn<CityProxy, java.lang.String> provinceCode() {
        return get("provinceCode");
    }

    /**
     * {@link City#getName}
     */
    public SQLColumn<CityProxy, java.lang.String> name() {
        return get("name");
    }

    /**
     * {@link City#getAreas}
     */
    public SQLNavigateColumn<CityProxy, com.easy.query.test.entity.base.Area> areas() {
        return get("areas", com.easy.query.test.entity.base.Area.class);
    }


    @Override
    public Class<City> getEntityClass() {
        return entityClass;
    }


    /**
     * 数据库列的简单获取
     *
     * @return
     */
    public CityProxyFetcher FETCHER = new CityProxyFetcher(this, null, SQLSelectAsExpression.empty);


    public static class CityProxyFetcher extends AbstractFetcher<CityProxy, City, CityProxyFetcher> {

        public CityProxyFetcher(CityProxy proxy, CityProxyFetcher prev, SQLSelectAsExpression sqlSelectAsExpression) {
            super(proxy, prev, sqlSelectAsExpression);
        }


        /**
         * {@link City#getCode}
         */
        public CityProxyFetcher code() {
            return add(getProxy().code());
        }

        /**
         * {@link City#getProvinceCode}
         */
        public CityProxyFetcher provinceCode() {
            return add(getProxy().provinceCode());
        }

        /**
         * {@link City#getName}
         */
        public CityProxyFetcher name() {
            return add(getProxy().name());
        }


        @Override
        protected CityProxyFetcher createFetcher(
                CityProxy cp,
                AbstractFetcher<CityProxy, City, CityProxyFetcher> prev,
                SQLSelectAsExpression sqlSelectExpression
        ) {
            return new CityProxyFetcher(cp, this, sqlSelectExpression);
        }
    }

}
