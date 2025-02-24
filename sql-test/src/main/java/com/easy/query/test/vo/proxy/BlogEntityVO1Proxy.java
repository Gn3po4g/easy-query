package com.easy.query.test.vo.proxy;

import com.easy.query.core.expression.parser.core.available.TableAvailable;
import com.easy.query.core.proxy.AbstractProxyEntity;
import com.easy.query.core.proxy.SQLColumn;
import com.easy.query.core.proxy.SQLSelectAsExpression;
import com.easy.query.core.proxy.fetcher.AbstractFetcher;
import com.easy.query.core.proxy.core.EntitySQLContext;
import com.easy.query.test.vo.BlogEntityVO1;
import com.easy.query.core.proxy.columns.types.SQLBigDecimalTypeColumn;
import com.easy.query.core.proxy.columns.types.SQLIntegerTypeColumn;
import com.easy.query.core.proxy.columns.types.SQLBooleanTypeColumn;

/**
 * this file automatically generated by easy-query, don't modify it
 * 当前文件是easy-query自动生成的请不要随意修改
 * 如果出现属性冲突请使用@ProxyProperty进行重命名
 *
 * @author easy-query
 */
public class BlogEntityVO1Proxy extends AbstractProxyEntity<BlogEntityVO1Proxy, BlogEntityVO1> {

    private static final Class<BlogEntityVO1> entityClass = BlogEntityVO1.class;

    public static final BlogEntityVO1Proxy TABLE = createTable().createEmpty();

    public static BlogEntityVO1Proxy createTable() {
        return new BlogEntityVO1Proxy();
    }

    public BlogEntityVO1Proxy() {
    }

    /**
     * 评分
     * {@link BlogEntityVO1#getScore}
     */
    public SQLBigDecimalTypeColumn<BlogEntityVO1Proxy> score() {
        return getBigDecimalTypeColumn("score");
    }

    /**
     * 状态
     * {@link BlogEntityVO1#getStatus}
     */
    public SQLIntegerTypeColumn<BlogEntityVO1Proxy> status() {
        return getIntegerTypeColumn("status");
    }

    /**
     * 排序
     * {@link BlogEntityVO1#getOrder}
     */
    public SQLBigDecimalTypeColumn<BlogEntityVO1Proxy> order() {
        return getBigDecimalTypeColumn("order");
    }

    /**
     * 是否置顶
     * {@link BlogEntityVO1#getIsTop}
     */
    public SQLBooleanTypeColumn<BlogEntityVO1Proxy> isTop() {
        return getBooleanTypeColumn("isTop");
    }

    /**
     * 是否置顶
     * {@link BlogEntityVO1#getTop}
     */
    public SQLBooleanTypeColumn<BlogEntityVO1Proxy> top() {
        return getBooleanTypeColumn("top");
    }


    @Override
    public Class<BlogEntityVO1> getEntityClass() {
        return entityClass;
    }


    /**
     * 数据库列的简单获取
     *
     * @return
     */
    public BlogEntityVO1ProxyFetcher FETCHER = new BlogEntityVO1ProxyFetcher(this, null, SQLSelectAsExpression.empty);


    public static class BlogEntityVO1ProxyFetcher extends AbstractFetcher<BlogEntityVO1Proxy, BlogEntityVO1, BlogEntityVO1ProxyFetcher> {

        public BlogEntityVO1ProxyFetcher(BlogEntityVO1Proxy proxy, BlogEntityVO1ProxyFetcher prev, SQLSelectAsExpression sqlSelectAsExpression) {
            super(proxy, prev, sqlSelectAsExpression);
        }


        /**
         * 评分
         * {@link BlogEntityVO1#getScore}
         */
        public BlogEntityVO1ProxyFetcher score() {
            return add(getProxy().score());
        }

        /**
         * 状态
         * {@link BlogEntityVO1#getStatus}
         */
        public BlogEntityVO1ProxyFetcher status() {
            return add(getProxy().status());
        }

        /**
         * 排序
         * {@link BlogEntityVO1#getOrder}
         */
        public BlogEntityVO1ProxyFetcher order() {
            return add(getProxy().order());
        }

        /**
         * 是否置顶
         * {@link BlogEntityVO1#getIsTop}
         */
        public BlogEntityVO1ProxyFetcher isTop() {
            return add(getProxy().isTop());
        }

        /**
         * 是否置顶
         * {@link BlogEntityVO1#getTop}
         */
        public BlogEntityVO1ProxyFetcher top() {
            return add(getProxy().top());
        }


        @Override
        protected BlogEntityVO1ProxyFetcher createFetcher(BlogEntityVO1Proxy cp, AbstractFetcher<BlogEntityVO1Proxy, BlogEntityVO1, BlogEntityVO1ProxyFetcher> prev, SQLSelectAsExpression sqlSelectExpression) {
            return new BlogEntityVO1ProxyFetcher(cp, this, sqlSelectExpression);
        }
    }

}
