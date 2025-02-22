package com.easy.query.core.util;

import com.easy.query.core.configuration.dialect.SQLKeyword;
import com.easy.query.core.exception.EasyQueryException;
import com.easy.query.core.metadata.EntityMetadata;

import java.util.function.Function;

/**
 * create time 2025/1/11 14:42
 * 文件说明
 *
 * @author xuejiaming
 */
public class EasyToSQLUtil {


    public static String getQuoteSQLName(SQLKeyword sqlKeyword, String val) {
        if (val == null) {
            return "";
        }
        if (val.contains(".")) {
            return String.join(".", EasyCollectionUtil.select(val.split("\\."), (s, i) -> sqlKeyword.getQuoteName(s)));
        } else {
            return sqlKeyword.getQuoteName(val);
        }
    }

    public static String getQuoteSQLName(SQLKeyword sqlKeyword, String val1, String val2) {
        boolean v1Blank = EasyStringUtil.isBlank(val1);
        boolean v2Blank = EasyStringUtil.isBlank(val2);
        if (v1Blank &&v2Blank) {
            return "";
        }
        if (v1Blank) {
            return getQuoteSQLName(sqlKeyword, val2);
        }
        if (v2Blank) {
            return getQuoteSQLName(sqlKeyword, val1);
        }
        return getQuoteSQLName(sqlKeyword, val1 + "." + val2);
    }

    public static String getSchemaTableName(SQLKeyword sqlKeyword, EntityMetadata entityMetadata, String entityTableName, Function<String, String> schemaAs, Function<String, String> tableNameAs) {
        String tableName = doGetTableName(entityMetadata, entityTableName, tableNameAs);
        String schema = getSchema(sqlKeyword, entityMetadata, entityMetadata.getSchemaOrNull(), schemaAs, null);
        if (EasyStringUtil.isNotBlank(schema)) {
            return getQuoteSQLName(sqlKeyword, schema) + "." + getQuoteSQLName(sqlKeyword, tableName);
        }
        return getQuoteSQLName(sqlKeyword, tableName);
    }

    public static String getTableName(SQLKeyword sqlKeyword, EntityMetadata entityMetadata, String tableName, Function<String, String> tableNameAs) {
        return sqlKeyword.getQuoteName(doGetTableName(entityMetadata, tableName, tableNameAs));
    }

    public static String getSchema(SQLKeyword sqlKeyword, EntityMetadata entityMetadata, String schema, Function<String, String> schemaAs, String def) {
        String entitySchema = doGetSchema(entityMetadata, schema, schemaAs);
        if (EasyStringUtil.isNotBlank(entitySchema)) {
            return getQuoteSQLName(sqlKeyword, entitySchema);
        }
        return def;
    }

    public static String getSchemaWithoutDatabaseName(EntityMetadata entityMetadata, String schema, Function<String, String> schemaAs, String def) {
        String entitySchema = doGetSchema(entityMetadata, schema, schemaAs);
        if (EasyStringUtil.isNotBlank(entitySchema)) {
            if (schema.contains(".")) {
                String[] split = entitySchema.split("\\.");
                return split[split.length - 1];
            } else {
                return entitySchema;
            }
        }
        return def;
    }

    private static String doGetSchema(EntityMetadata entityMetadata, String schema, Function<String, String> schemaAs) {
        if (schema != null || schemaAs != null) {
            if (schemaAs != null) {
                return schemaAs.apply(schema);
            }
            return schema;
        }
        return null;
    }

    private static String doGetTableName(EntityMetadata entityMetadata, String tableName, Function<String, String> tableNameAs) {
        if (tableNameAs != null) {
            String applyTableName = tableNameAs.apply(tableName);
            return checkTableName(applyTableName, entityMetadata.getEntityClass());
        }
        return checkTableName(tableName, entityMetadata.getEntityClass());
    }

    private static String checkTableName(String tableName, Class<?> entityClass) {

        if (tableName == null) {
            throw new EasyQueryException("table " + EasyClassUtil.getSimpleName(entityClass) + " cant found mapping table name");
        }
        return tableName;
    }
}
