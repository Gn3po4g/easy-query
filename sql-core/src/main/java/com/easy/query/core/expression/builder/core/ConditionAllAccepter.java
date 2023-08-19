package com.easy.query.core.expression.builder.core;

import com.easy.query.core.expression.parser.core.available.TableAvailable;

/**
 * create time 2023/8/19 15:06
 * 文件说明
 *
 * @author xuejiaming
 */
public class ConditionAllAccepter implements ConditionAccepter {
    public static final ConditionAccepter DEFAULT=new ConditionAllAccepter();
    private ConditionAllAccepter(){

    }
    @Override
    public boolean accept(TableAvailable table, String property, Object value) {
        return true;
    }
}
