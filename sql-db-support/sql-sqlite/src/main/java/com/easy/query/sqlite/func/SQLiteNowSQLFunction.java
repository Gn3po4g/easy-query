package com.easy.query.sqlite.func;

import com.easy.query.core.expression.parser.core.available.TableAvailable;
import com.easy.query.core.expression.parser.core.base.scec.core.SQLNativeChainExpressionContext;
import com.easy.query.core.func.SQLFunction;
import com.easy.query.core.func.def.AbstractSQLFunction;

/**
 * create time 2023/10/14 23:31
 * 文件说明
 *
 * @author xuejiaming
 */
public class SQLiteNowSQLFunction extends AbstractSQLFunction {
    public static final SQLFunction INSTANCE=new SQLiteNowSQLFunction();
    @Override
    public String sqlSegment(TableAvailable defaultTable) {
        return "datetime(current_timestamp,'localtime')";
    }

    @Override
    public int paramMarks() {
        return 0;
    }

    @Override
    protected void consume0(SQLNativeChainExpressionContext context) {

    }
}
