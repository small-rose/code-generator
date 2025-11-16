package com.small.rose.core.service;

import com.small.rose.core.bean.ColumnMeta;
import com.small.rose.core.bean.DataSourceConfig;
import com.small.rose.core.bean.TableMeta;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Project: code-generator
 * @Author: 张小菜
 * @Description: [ MetadataExtractor ] 说明： 无
 * @Function: 功能描述： 无
 * @Date: 2025/11/16 016 23:10
 * @Version: v1.0
 */

@Slf4j
@AllArgsConstructor
@Component
public class MetadataExtractor {


    private final TypeConverter typeConverter;

    /**
     * 提取所有表信息
     */
    public List<TableMeta> extractTables(DataSourceConfig config) {
        List<TableMeta> tables = new ArrayList<>();

        try (Connection conn = createDataSource(config).getConnection()) {
            // 先测试连接
            testConnection(config);

            DatabaseMetaData metaData = conn.getMetaData();

            // 获取数据库名称（catalog）
            String catalog = conn.getCatalog();
            if (catalog == null) {
                catalog = extractCatalogFromUrl(config.getUrl());
            }

            log.info("开始提取表信息，数据库: {}", catalog);

            // 获取所有表
            ResultSet tableRs = metaData.getTables(catalog, null, "%", new String[]{"TABLE"});

            int tableCount = 0;
            while (tableRs.next()) {
                String tableName = tableRs.getString("TABLE_NAME");
                String tableComment = tableRs.getString("REMARKS");

                // 跳过系统表
                if (isSystemTable(tableName)) {
                    log.debug("跳过系统表: {}", tableName);
                    continue;
                }

                TableMeta table = extractTable(config, tableName);
                table.setTableComment(tableComment != null ? tableComment : tableName);
                tables.add(table);
                tableCount++;

                log.debug("提取表: {} - {}", tableName, tableComment);
            }

            log.info("成功提取 {} 张表", tableCount);

        } catch (SQLException e) {
            throw new RuntimeException("提取表信息失败: " + e.getMessage(), e);
        }

        return tables;
    }

    /**
     * 提取单表信息
     */
    public TableMeta extractTable(DataSourceConfig config, String tableName) {
        TableMeta table = new TableMeta();
        table.setTableName(tableName);
        table.setClassName(toCamelCase(tableName, true));
        table.setInstanceName(toCamelCase(tableName, false));

        try (Connection conn = createDataSource(config).getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();

            // 获取表注释
            String tableComment = getTableComment(conn, tableName);
            table.setTableComment(tableComment != null ? tableComment : tableName);

            // 获取列信息
            List<ColumnMeta> columns = extractColumns(metaData, tableName);
            table.setColumns(columns);

            // 设置主键
            setPrimaryKey(table, metaData, tableName);

        } catch (SQLException e) {
            throw new RuntimeException("提取表" + tableName + "信息失败: " + e.getMessage(), e);
        }

        return table;
    }

    /**
     * 提取列信息
     */
    private List<ColumnMeta> extractColumns(DatabaseMetaData metaData, String tableName) throws SQLException {
        List<ColumnMeta> columns = new ArrayList<>();
        Set<String> primaryKeys = getPrimaryKeys(metaData, tableName);

        ResultSet columnRs = metaData.getColumns(null, null, tableName, "%");
        while (columnRs.next()) {
            ColumnMeta column = new ColumnMeta();

            // 基础信息
            column.setColumnName(columnRs.getString("COLUMN_NAME"));
            column.setPropertyName(toCamelCase(column.getColumnName(), false));
            column.setDataType(columnRs.getString("TYPE_NAME"));
            column.setColumnComment(columnRs.getString("REMARKS"));
            column.setLength(columnRs.getInt("COLUMN_SIZE"));
            column.setScale(columnRs.getInt("DECIMAL_DIGITS"));
            column.setNullable("YES".equals(columnRs.getString("IS_NULLABLE")));
            column.setDefaultValue(columnRs.getString("COLUMN_DEF"));

            // 类型转换
            column.setJavaType(typeConverter.toJavaType(column.getDataType()));
            column.setJdbcType(typeConverter.toJdbcType(column.getDataType()));

            // 是否主键
            column.setIsPrimaryKey(primaryKeys.contains(column.getColumnName()));

            // 是否自增
            column.setIsAutoIncrement("YES".equals(columnRs.getString("IS_AUTOINCREMENT")));

            columns.add(column);
        }

        return columns;
    }

    /**
     * 获取主键信息
     */
    private Set<String> getPrimaryKeys(DatabaseMetaData metaData, String tableName) throws SQLException {
        Set<String> primaryKeys = new HashSet<>();

        ResultSet pkRs = metaData.getPrimaryKeys(null, null, tableName);
        while (pkRs.next()) {
            primaryKeys.add(pkRs.getString("COLUMN_NAME"));
        }

        return primaryKeys;
    }

    /**
     * 设置主键信息
     */
    private void setPrimaryKey(TableMeta table, DatabaseMetaData metaData, String tableName) throws SQLException {
        for (ColumnMeta column : table.getColumns()) {
            if (column.isPrimaryKey()) {
                table.setPrimaryKey(column);
                break;
            }
        }
    }

    /**
     * 获取表注释（MySQL特殊处理）
     */
    private String getTableComment(Connection conn, String tableName) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        String productName = metaData.getDatabaseProductName().toLowerCase();

        if (productName.contains("mysql")) {
            return getMySQLTableComment(conn, tableName);
        }
        return null;
    }

    private String getMySQLTableComment(Connection conn, String tableName) throws SQLException {
        String sql = "SELECT TABLE_COMMENT FROM information_schema.TABLES " +
                "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tableName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("TABLE_COMMENT");
            }
        }
        return null;
    }

    /**
     * 创建数据源
     */
    private DataSource createDataSource(DataSourceConfig config) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(config.getUrl());
        dataSource.setUsername(config.getUsername());
        dataSource.setPassword(config.getPassword());

        // 自动推断驱动类
        String driverClassName = inferDriverClassName(config.getUrl());
        dataSource.setDriverClassName(driverClassName);

        return dataSource;
    }

    /**
     * 根据URL推断驱动类
     */
    private String inferDriverClassName(String url) {
        if (url.startsWith("jdbc:mysql:")) {
            return "com.mysql.cj.jdbc.Driver";
        } else if (url.startsWith("jdbc:oracle:")) {
            return "oracle.jdbc.OracleDriver";
        } else if (url.startsWith("jdbc:postgresql:")) {
            return "org.postgresql.Driver";
        } else if (url.startsWith("jdbc:sqlserver:")) {
            return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        }
        return "com.mysql.cj.jdbc.Driver"; // 默认MySQL
    }

    /**
     * 下划线转驼峰
     */
    private String toCamelCase(String name, boolean firstCharUpper) {
        if (name == null || name.isEmpty()) return name;

        StringBuilder result = new StringBuilder();
        String[] parts = name.split("_");

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (part.isEmpty()) continue;

            if (i == 0 && !firstCharUpper) {
                result.append(Character.toLowerCase(part.charAt(0)));
            } else {
                result.append(Character.toUpperCase(part.charAt(0)));
            }

            if (part.length() > 1) {
                result.append(part.substring(1).toLowerCase());
            }
        }

        return result.toString();
    }

    /**
     * 判断是否为系统表
     */
    private boolean isSystemTable(String tableName) {
        if (tableName == null) return true;

        String lowerName = tableName.toLowerCase();
        return lowerName.startsWith("sys_") ||
                lowerName.startsWith("qrtz_") ||
                lowerName.startsWith("act_") ||
                lowerName.contains("$") ||
                lowerName.matches("^sys.*");
    }

    /**
     * 测试数据库连接
     */
    public void testConnection(DataSourceConfig config) throws SQLException {
        Connection conn = null;
        try {
            DataSource dataSource = createDataSource(config);
            conn = dataSource.getConnection();

            DatabaseMetaData metaData = conn.getMetaData();
            String productName = metaData.getDatabaseProductName();
            String productVersion = metaData.getDatabaseProductVersion();

            log.info("✅ 数据库连接测试成功");
            log.info("数据库产品: {} {}", productName, productVersion);
            log.info("URL: {}", metaData.getURL());
            log.info("用户名: {}", metaData.getUserName());

            // 测试查询
            testQuery(conn);

        } catch (SQLException e) {
            log.error("❌ 数据库连接测试失败", e);
            throw new SQLException("数据库连接失败: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    log.warn("关闭连接时发生错误", e);
                }
            }
        }
    }

    /**
     * 执行简单查询测试连接有效性
     */
    private void testQuery(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            // 尝试执行简单查询
            String testQuery = getTestQuery(conn);
            try (ResultSet rs = stmt.executeQuery(testQuery)) {
                if (rs.next()) {
                    log.info("✅ 查询测试通过");
                }
            }
        } catch (SQLException e) {
            log.warn("查询测试失败，但连接已建立: {}", e.getMessage());
            // 连接已建立，查询失败不影响连接测试
        }
    }

    /**
     * 根据数据库类型获取测试查询语句
     */
    private String getTestQuery(Connection conn) throws SQLException {
        String productName = conn.getMetaData().getDatabaseProductName().toLowerCase();

        if (productName.contains("mysql")) {
            return "SELECT 1";
        } else if (productName.contains("oracle")) {
            return "SELECT 1 FROM DUAL";
        } else if (productName.contains("postgresql")) {
            return "SELECT 1";
        } else if (productName.contains("sqlserver")) {
            return "SELECT 1";
        } else {
            return "SELECT 1";
        }
    }



    /**
     * 从URL提取数据库名称
     */
    private String extractCatalogFromUrl(String url) {
        if (url == null) return null;

        // 从jdbc:mysql://localhost:3306/database_name中提取数据库名
        if (url.contains("/")) {
            String[] parts = url.split("/");
            if (parts.length > 3) {
                String dbPart = parts[parts.length - 1];
                if (dbPart.contains("?")) {
                    return dbPart.split("\\?")[0];
                }
                return dbPart;
            }
        }
        return null;
    }
}
