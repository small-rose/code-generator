package com.small.rose.core.service;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Project: code-generator
 * @Author: 张小菜
 * @Description: [ TypeConverter ] 说明： 无
 * @Function: 功能描述： 无
 * @Date: 2025/11/16 016 23:11
 * @Version: v1.0
 */
@Component
public class TypeConverter {

    private static final Map<String, String> TYPE_MAPPING = new HashMap<>();
    private static final Map<String, String> JDBC_TYPE_MAPPING = new HashMap<>();

    static {
        initTypeMappings();
    }

    private static void initTypeMappings() {
        // MySQL类型映射
        TYPE_MAPPING.put("tinyint", "Integer");
        TYPE_MAPPING.put("smallint", "Integer");
        TYPE_MAPPING.put("mediumint", "Integer");
        TYPE_MAPPING.put("int", "Integer");
        TYPE_MAPPING.put("integer", "Integer");
        TYPE_MAPPING.put("bigint", "Long");
        TYPE_MAPPING.put("float", "Float");
        TYPE_MAPPING.put("double", "Double");
        TYPE_MAPPING.put("decimal", "java.math.BigDecimal");
        TYPE_MAPPING.put("numeric", "java.math.BigDecimal");
        TYPE_MAPPING.put("char", "String");
        TYPE_MAPPING.put("varchar", "String");
        TYPE_MAPPING.put("text", "String");
        TYPE_MAPPING.put("longtext", "String");
        TYPE_MAPPING.put("mediumtext", "String");
        TYPE_MAPPING.put("date", "java.time.LocalDate");
        TYPE_MAPPING.put("datetime", "java.time.LocalDateTime");
        TYPE_MAPPING.put("timestamp", "java.time.LocalDateTime");
        TYPE_MAPPING.put("time", "java.time.LocalTime");
        TYPE_MAPPING.put("year", "Integer");
        TYPE_MAPPING.put("binary", "byte[]");
        TYPE_MAPPING.put("varbinary", "byte[]");
        TYPE_MAPPING.put("blob", "byte[]");
        TYPE_MAPPING.put("longblob", "byte[]");
        TYPE_MAPPING.put("bit", "Boolean");
        TYPE_MAPPING.put("json", "String");
        TYPE_MAPPING.put("enum", "String");
        TYPE_MAPPING.put("set", "String");

        // JDBC类型映射
        JDBC_TYPE_MAPPING.put("tinyint", "TINYINT");
        JDBC_TYPE_MAPPING.put("smallint", "SMALLINT");
        JDBC_TYPE_MAPPING.put("int", "INTEGER");
        JDBC_TYPE_MAPPING.put("integer", "INTEGER");
        JDBC_TYPE_MAPPING.put("bigint", "BIGINT");
        JDBC_TYPE_MAPPING.put("float", "FLOAT");
        JDBC_TYPE_MAPPING.put("double", "DOUBLE");
        JDBC_TYPE_MAPPING.put("decimal", "DECIMAL");
        JDBC_TYPE_MAPPING.put("numeric", "NUMERIC");
        JDBC_TYPE_MAPPING.put("char", "CHAR");
        JDBC_TYPE_MAPPING.put("varchar", "VARCHAR");
        JDBC_TYPE_MAPPING.put("text", "LONGVARCHAR");
        JDBC_TYPE_MAPPING.put("date", "DATE");
        JDBC_TYPE_MAPPING.put("datetime", "TIMESTAMP");
        JDBC_TYPE_MAPPING.put("timestamp", "TIMESTAMP");
        JDBC_TYPE_MAPPING.put("time", "TIME");
        JDBC_TYPE_MAPPING.put("blob", "BLOB");
        JDBC_TYPE_MAPPING.put("bit", "BIT");
        JDBC_TYPE_MAPPING.put("json", "VARCHAR");
    }

    /**
     * 转换为Java类型
     */
    public String toJavaType(String dbType) {
        if (dbType == null) return "String";

        // 处理带长度的类型，如varchar(255)
        String typeName = dbType.toLowerCase().split("\\(")[0].trim();

        // 处理无符号类型
        if (typeName.contains("unsigned")) {
            typeName = typeName.replace("unsigned", "").trim();
            // 无符号类型升级
            if ("tinyint".equals(typeName) || "smallint".equals(typeName) || "mediumint".equals(typeName)) {
                return "Integer";
            } else if ("int".equals(typeName) || "integer".equals(typeName)) {
                return "Long";
            } else if ("bigint".equals(typeName)) {
                return "java.math.BigInteger";
            }
        }

        return TYPE_MAPPING.getOrDefault(typeName, "String");
    }

    /**
     * 转换为JDBC类型
     */
    public String toJdbcType(String dbType) {
        if (dbType == null) return "VARCHAR";

        String typeName = dbType.toLowerCase().split("\\(")[0].trim();
        return JDBC_TYPE_MAPPING.getOrDefault(typeName, "VARCHAR");
    }

    /**
     * 是否需要导入包
     */
    public boolean needImport(String javaType) {
        return javaType.startsWith("java.time.") ||
                javaType.startsWith("java.math.") ||
                javaType.equals("byte[]");
    }
}
