package com.easy.query.core.func;

import com.easy.query.core.expression.parser.core.available.TableAvailable;
import com.easy.query.core.func.def.DateTimeFormatSQLFunction;
import com.easy.query.core.func.def.IfNullSQLFunction;

/**
 * create time 2023/10/5 22:23
 * 文件说明
 *
 * @author xuejiaming
 */
public class DefaultSQLFunc implements SQLFunc{

    @Override
    public SQLFunction ifNull(TableAvailable table, String property, Object def) {
        return new IfNullSQLFunction(table,property,def);
    }

    @Override
    public SQLFunction dateTimeFormat(TableAvailable table, String property, String javaFormat) {
        return new DateTimeFormatSQLFunction(table,property,javaFormat);
    }
}
