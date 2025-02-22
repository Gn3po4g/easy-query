package com.easy.query.test.mssql;

import com.easy.query.api4j.select.Queryable;
import com.easy.query.core.api.pagination.EasyPageResult;
import com.easy.query.core.basic.extension.listener.JdbcExecuteAfterArg;
import com.easy.query.core.proxy.core.draft.Draft2;
import com.easy.query.core.proxy.sql.GroupKeys;
import com.easy.query.core.proxy.sql.Select;
import com.easy.query.core.util.EasySQLUtil;
import com.easy.query.test.listener.ListenerContext;
import com.easy.query.test.mssql.entity.MsSQLMyTopic;
import com.easy.query.test.mssql.entity.MsSQLMyTopic1;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * create time 2023/7/27 17:34
 * 文件说明
 *
 * @author xuejiaming
 */
public class MsSQLQueryTest extends MsSQLBaseTest{

    @Test
    public void query0() {
        Queryable<MsSQLMyTopic> queryable = easyQuery.queryable(MsSQLMyTopic.class)
                .where(o -> o.eq(MsSQLMyTopic::getId, "123xxx"));
        String sql = queryable.toSQL();
        Assert.assertEquals("SELECT [Id],[Stars],[Title],[CreateTime] FROM [MyTopic] WHERE [Id] = ?", sql);
        MsSQLMyTopic msSQLMyTopic = queryable.firstOrNull();
        Assert.assertNull(msSQLMyTopic);
    }
    @Test
    public void query1() {
        Queryable<MsSQLMyTopic> queryable = easyQuery.queryable(MsSQLMyTopic.class)
                .where(o -> o.eq(MsSQLMyTopic::getId, "1"));
        String sql = queryable.toSQL();
        Assert.assertEquals("SELECT [Id],[Stars],[Title],[CreateTime] FROM [MyTopic] WHERE [Id] = ?", sql);
        MsSQLMyTopic msSQLMyTopic = queryable.firstOrNull();
        Assert.assertNotNull(msSQLMyTopic);
        Assert.assertNotNull(msSQLMyTopic.getId());
        Assert.assertNotNull(msSQLMyTopic.getStars());
        Assert.assertNotNull(msSQLMyTopic.getTitle());
        Assert.assertNotNull(msSQLMyTopic.getCreateTime());
    }
    @Test
    public void query2() {
        Queryable<MsSQLMyTopic1> queryable = easyQuery.queryable(MsSQLMyTopic1.class)
                .where(o -> o.eq(MsSQLMyTopic1::getId, "123xxx"));
        String sql = queryable.toSQL();
        Assert.assertEquals("SELECT [Id],[Stars],[Title],[CreateTime] FROM [MyTopic] WHERE [Id] = ?", sql);
        MsSQLMyTopic1 msSQLMyTopic = queryable.firstOrNull();
        Assert.assertNull(msSQLMyTopic);
    }
    @Test
    public void query3() {
        Queryable<MsSQLMyTopic1> queryable = easyQuery.queryable(MsSQLMyTopic1.class)
                .where(o -> o.eq(MsSQLMyTopic1::getId, "1"));
        String sql = queryable.toSQL();
        Assert.assertEquals("SELECT [Id],[Stars],[Title],[CreateTime] FROM [MyTopic] WHERE [Id] = ?", sql);
        MsSQLMyTopic1 msSQLMyTopic = queryable.firstOrNull();
        Assert.assertNotNull(msSQLMyTopic);
        Assert.assertNotNull(msSQLMyTopic.getId());
        Assert.assertNotNull(msSQLMyTopic.getStars());
        Assert.assertNotNull(msSQLMyTopic.getTitle());
        Assert.assertNotNull(msSQLMyTopic.getCreateTime());
    }
    @Test
    public void query4() {

        EasyPageResult<MsSQLMyTopic> topicPageResult = easyQuery
                .queryable(MsSQLMyTopic.class)
                .where(o -> o.isNotNull(MsSQLMyTopic::getId))
                .toPageResult(2, 20);
        List<MsSQLMyTopic> data = topicPageResult.getData();
        Assert.assertEquals(20, data.size());
    }
    @Test
    public void query5() {

        EasyPageResult<MsSQLMyTopic> topicPageResult = easyQuery
                .queryable(MsSQLMyTopic.class)
                .where(o -> o.isNotNull(MsSQLMyTopic::getId))
                .orderByAsc(o->o.column(MsSQLMyTopic::getId))
                .toPageResult(2, 20);
        List<MsSQLMyTopic> data = topicPageResult.getData();
        Assert.assertEquals(20, data.size());
    }
    @Test
    public void query6() {

        EasyPageResult<MsSQLMyTopic> topicPageResult = easyQuery
                .queryable(MsSQLMyTopic.class)
                .where(o -> o.isNotNull(MsSQLMyTopic::getId))
                .orderByAsc(o->o.column(MsSQLMyTopic::getStars))
                .toPageResult(2, 20);
        List<MsSQLMyTopic> data = topicPageResult.getData();
        Assert.assertEquals(20, data.size());
        for (int i = 0; i < 20; i++) {
            MsSQLMyTopic msSQLMyTopic = data.get(i);
            Assert.assertEquals(msSQLMyTopic.getId(),String.valueOf(i+20) );
            Assert.assertEquals(msSQLMyTopic.getStars(),(Integer)(i+120) );
        }
    }
    
    @Test
    public void query7(){

        ListenerContext listenerContext = new ListenerContext();
        listenerContextManager.startListen(listenerContext);



        List<MsSQLMyTopic> list = entityQuery.queryable(MsSQLMyTopic.class)
                .where(o -> {
                    o.title().compareTo("1").eq(1);
                }).toList();
        Assert.assertNotNull(listenerContext.getJdbcExecuteAfterArg());
        JdbcExecuteAfterArg jdbcExecuteAfterArg = listenerContext.getJdbcExecuteAfterArg();
        Assert.assertEquals("SELECT [Id],[Stars],[Title],[CreateTime] FROM [MyTopic] WHERE (CASE WHEN [Title] = ? THEN 0 WHEN [Title] > ? THEN 1 ELSE -1 END) = ?", jdbcExecuteAfterArg.getBeforeArg().getSql());
        Assert.assertEquals("1(String),1(String),1(Integer)", EasySQLUtil.sqlParameterToString(jdbcExecuteAfterArg.getBeforeArg().getSqlParameters().get(0)));
        listenerContextManager.clear();
    }
    @Test
    public void query8(){

        ListenerContext listenerContext = new ListenerContext();
        listenerContextManager.startListen(listenerContext);



        List<MsSQLMyTopic> list = entityQuery.queryable(MsSQLMyTopic.class)
                .where(o -> {
                    o.title().compareTo(o.id()).eq(1);
                }).toList();
        Assert.assertNotNull(listenerContext.getJdbcExecuteAfterArg());
        JdbcExecuteAfterArg jdbcExecuteAfterArg = listenerContext.getJdbcExecuteAfterArg();
        Assert.assertEquals("SELECT [Id],[Stars],[Title],[CreateTime] FROM [MyTopic] WHERE (CASE WHEN [Title] = [Id] THEN 0 WHEN [Title] > [Id] THEN 1 ELSE -1 END) = ?", jdbcExecuteAfterArg.getBeforeArg().getSql());
        Assert.assertEquals("1(Integer)", EasySQLUtil.sqlParameterToString(jdbcExecuteAfterArg.getBeforeArg().getSqlParameters().get(0)));
        listenerContextManager.clear();
    }
    @Test
    public void query9(){
//        String sql = easyQuery.queryable("select * from t_order", Map.class)
//                .limit(10, 20).toSQL();
//        {
//
//            List<Map> list = easyQuery.queryable("select * from t_order", Map.class)
//                    .limit(10,20).toList();
//        }

        ListenerContext listenerContext = new ListenerContext();
        listenerContextManager.startListen(listenerContext);

        List<Draft2<String, String>> list = entityQuery.queryable(MsSQLMyTopic.class)
                .groupBy(o -> GroupKeys.of(o.title()))
                .select(o -> Select.DRAFT.of(
                        o.key1(),
                        o.groupTable().id().join(",")
                )).toList();
        Assert.assertNotNull(listenerContext.getJdbcExecuteAfterArg());
        JdbcExecuteAfterArg jdbcExecuteAfterArg = listenerContext.getJdbcExecuteAfterArg();
        Assert.assertEquals("SELECT t.[Title] AS [Value1],STRING_AGG(t.[Id], ?) AS [Value2] FROM [MyTopic] t GROUP BY t.[Title]", jdbcExecuteAfterArg.getBeforeArg().getSql());
        Assert.assertEquals(",(String)", EasySQLUtil.sqlParameterToString(jdbcExecuteAfterArg.getBeforeArg().getSqlParameters().get(0)));
        listenerContextManager.clear();
    }

    @Test
    public void query10(){

        ListenerContext listenerContext = new ListenerContext();
        listenerContextManager.startListen(listenerContext);

        List<MsSQLMyTopic> aa = entityQuery.queryable(MsSQLMyTopic.class)
                .where(m -> {
                    m.createTime().format("yyyy年MM月dd日").eq("2022年01月01日");
                }).toList();

        Assert.assertNotNull(listenerContext.getJdbcExecuteAfterArg());
        JdbcExecuteAfterArg jdbcExecuteAfterArg = listenerContext.getJdbcExecuteAfterArg();
        Assert.assertEquals("SELECT [Id],[Stars],[Title],[CreateTime] FROM [MyTopic] WHERE ( '' + SUBSTRING(CONVERT(CHAR(8), [CreateTime], 112), 1, 4) + '年' + SUBSTRING(CONVERT(CHAR(6), [CreateTime], 12), 3, 2) + '月' + SUBSTRING(CONVERT(CHAR(6), [CreateTime], 12), 5, 2) + '日') = ?", jdbcExecuteAfterArg.getBeforeArg().getSql());
        Assert.assertEquals("2022年01月01日(String)", EasySQLUtil.sqlParameterToString(jdbcExecuteAfterArg.getBeforeArg().getSqlParameters().get(0)));
        listenerContextManager.clear();
    }
    @Test
    public void query11(){

        ListenerContext listenerContext = new ListenerContext();
        listenerContextManager.startListen(listenerContext);

        List<MsSQLMyTopic> aa = entityQuery.queryable(MsSQLMyTopic.class)
                .where(m -> {
                    m.createTime().format("yyyy年MM月dd日 HH时mm分ss秒").eq("2022年01月01日 01时01分01秒");
                }).toList();

        Assert.assertNotNull(listenerContext.getJdbcExecuteAfterArg());
        JdbcExecuteAfterArg jdbcExecuteAfterArg = listenerContext.getJdbcExecuteAfterArg();
        Assert.assertEquals("SELECT [Id],[Stars],[Title],[CreateTime] FROM [MyTopic] WHERE ( '' + SUBSTRING(CONVERT(CHAR(8), [CreateTime], 112), 1, 4) + '年' + SUBSTRING(CONVERT(CHAR(6), [CreateTime], 12), 3, 2) + '月' + SUBSTRING(CONVERT(CHAR(6), [CreateTime], 12), 5, 2) + '日 ' + SUBSTRING(CONVERT(CHAR(8), [CreateTime], 24), 1, 2) + '时' + SUBSTRING(CONVERT(CHAR(8), [CreateTime], 24), 4, 2) + '分' + SUBSTRING(CONVERT(CHAR(8), [CreateTime], 24), 7, 2) + '秒') = ?", jdbcExecuteAfterArg.getBeforeArg().getSql());
        Assert.assertEquals("2022年01月01日 01时01分01秒(String)", EasySQLUtil.sqlParameterToString(jdbcExecuteAfterArg.getBeforeArg().getSqlParameters().get(0)));
        listenerContextManager.clear();
    }
    @Test
    public void update1(){

        ListenerContext listenerContext = new ListenerContext();
        listenerContextManager.startListen(listenerContext);
        entityQuery.updatable(MsSQLMyTopic.class)
                .setColumns(m -> m.title().set("123xx"))
                .where(m -> {
                    m.id().isNull();
                    m.expression().exists(()->{
                        return entityQuery.queryable(MsSQLMyTopic.class)
                                .where(m1 -> {m1.id().eq(m.id());});
                    });
                }).executeRows();
        Assert.assertNotNull(listenerContext.getJdbcExecuteAfterArg());
        JdbcExecuteAfterArg jdbcExecuteAfterArg = listenerContext.getJdbcExecuteAfterArg();
        Assert.assertEquals("UPDATE t SET t.[Title] = ? FROM [MyTopic] t WHERE t.[Id] IS NULL AND EXISTS (SELECT 1 FROM [MyTopic] t1 WHERE t1.[Id] = t.[Id])", jdbcExecuteAfterArg.getBeforeArg().getSql());
        Assert.assertEquals("123xx(String)", EasySQLUtil.sqlParameterToString(jdbcExecuteAfterArg.getBeforeArg().getSqlParameters().get(0)));
        listenerContextManager.clear();
    }

    @Test
     public void testLikeConcat1(){

//        List<Map<String, Object>> maps = entityQuery.sqlQueryMap("SELECT [Id],[Stars],[Title],[CreateTime] FROM [MyTopic] WHERE [Title] LIKE ('%'+(CAST(? + [Id] + ? AS NVARCHAR(MAX))+'%')");




        ListenerContext listenerContext = new ListenerContext();
        listenerContextManager.startListen(listenerContext);
        List<MsSQLMyTopic> list = entityQuery.queryable(MsSQLMyTopic.class)
                .where(m -> {
                    m.title().like(
                            m.expression().concat(s -> {
                                s.value("%");
                                s.expression(m.id());
                                s.value("%");
                            })
                    );
//                    m.title().likeMatchLeft(
//                            m.expression().concat(s -> {
//                                s.value("%");
//                                s.expression(m.id());
//                                s.value("%");
//                            })
//                    );
//                    m.title().likeMatchRight(
//                            m.expression().concat(s -> {
//                                s.value("%");
//                                s.expression(m.id());
//                                s.value("%");
//                            })
//                    );
                }).toList();
        Assert.assertNotNull(listenerContext.getJdbcExecuteAfterArg());
        JdbcExecuteAfterArg jdbcExecuteAfterArg = listenerContext.getJdbcExecuteAfterArg();
        Assert.assertEquals("SELECT [Id],[Stars],[Title],[CreateTime] FROM [MyTopic] WHERE [Title] LIKE ('%'+CAST(? + [Id] + ? AS NVARCHAR(MAX))+'%')", jdbcExecuteAfterArg.getBeforeArg().getSql());
        Assert.assertEquals("%(String),%(String)", EasySQLUtil.sqlParameterToString(jdbcExecuteAfterArg.getBeforeArg().getSqlParameters().get(0)));
        listenerContextManager.clear();
    }
    @Test
     public void testLikeConcat2(){

//        List<Map<String, Object>> maps = entityQuery.sqlQueryMap("SELECT [Id],[Stars],[Title],[CreateTime] FROM [MyTopic] WHERE [Title] LIKE ('%'+(CAST(? + [Id] + ? AS NVARCHAR(MAX))+'%')");




        ListenerContext listenerContext = new ListenerContext();
        listenerContextManager.startListen(listenerContext);
        List<MsSQLMyTopic> list = entityQuery.queryable(MsSQLMyTopic.class)
                .where(m -> {
//                    m.title().like(
//                            m.expression().concat(s -> {
//                                s.value("%");
//                                s.expression(m.id());
//                                s.value("%");
//                            })
//                    );
                    m.title().likeMatchLeft(
                            m.expression().concat(s -> {
                                s.value("%");
                                s.expression(m.id());
                                s.value("%");
                            })
                    );
//                    m.title().likeMatchRight(
//                            m.expression().concat(s -> {
//                                s.value("%");
//                                s.expression(m.id());
//                                s.value("%");
//                            })
//                    );
                }).toList();
        Assert.assertNotNull(listenerContext.getJdbcExecuteAfterArg());
        JdbcExecuteAfterArg jdbcExecuteAfterArg = listenerContext.getJdbcExecuteAfterArg();
        Assert.assertEquals("SELECT [Id],[Stars],[Title],[CreateTime] FROM [MyTopic] WHERE [Title] LIKE (CAST(? + [Id] + ? AS NVARCHAR(MAX))+'%')", jdbcExecuteAfterArg.getBeforeArg().getSql());
        Assert.assertEquals("%(String),%(String)", EasySQLUtil.sqlParameterToString(jdbcExecuteAfterArg.getBeforeArg().getSqlParameters().get(0)));
        listenerContextManager.clear();
    }
    @Test
     public void testLikeConcat3(){

//        List<Map<String, Object>> maps = entityQuery.sqlQueryMap("SELECT [Id],[Stars],[Title],[CreateTime] FROM [MyTopic] WHERE [Title] LIKE ('%'+(CAST(? + [Id] + ? AS NVARCHAR(MAX))+'%')");




        ListenerContext listenerContext = new ListenerContext();
        listenerContextManager.startListen(listenerContext);
        List<MsSQLMyTopic> list = entityQuery.queryable(MsSQLMyTopic.class)
                .where(m -> {
//                    m.title().like(
//                            m.expression().concat(s -> {
//                                s.value("%");
//                                s.expression(m.id());
//                                s.value("%");
//                            })
//                    );
//                    m.title().likeMatchLeft(
//                            m.expression().concat(s -> {
//                                s.value("%");
//                                s.expression(m.id());
//                                s.value("%");
//                            })
//                    );
                    m.title().likeMatchRight(
                            m.expression().concat(s -> {
                                s.value("%");
                                s.expression(m.id());
                                s.value("%");
                            })
                    );
                }).toList();
        Assert.assertNotNull(listenerContext.getJdbcExecuteAfterArg());
        JdbcExecuteAfterArg jdbcExecuteAfterArg = listenerContext.getJdbcExecuteAfterArg();
        Assert.assertEquals("SELECT [Id],[Stars],[Title],[CreateTime] FROM [MyTopic] WHERE [Title] LIKE ('%'+CAST(? + [Id] + ? AS NVARCHAR(MAX)))", jdbcExecuteAfterArg.getBeforeArg().getSql());
        Assert.assertEquals("%(String),%(String)", EasySQLUtil.sqlParameterToString(jdbcExecuteAfterArg.getBeforeArg().getSqlParameters().get(0)));
        listenerContextManager.clear();
    }
}
