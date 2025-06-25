package com.easy.query.core.api.dynamic.executor.search.match;

import com.easy.query.core.expression.parser.core.available.TableAvailable;

public class EasyTableAliasMatch
        implements EasyTableMatch {
    private final String alias;

    public EasyTableAliasMatch(String alias) {
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

    @Override
    public boolean match(TableAvailable table, int tableIndex) {
        return alias.equals(table.getAlias());
    }

    @Override
    public String toString() {
        return "EasyTableAliasMatch：" + alias;
    }
}
