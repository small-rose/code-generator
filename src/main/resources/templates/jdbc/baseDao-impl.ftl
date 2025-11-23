package ${config.packageName}.dao.impl;

import ${config.packageName}.entity.${table.className};
import ${config.packageName}.dao.${table.className}Dao;
import ${config.packageName}.dao.base.impl.BaseDaoImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Arrays;
import java.util.List;

/**
* ${table.tableComment!table.tableName} DAO实现类
*
* @author ${config.author}
* @since ${.now?string("yyyy-MM-dd")}
*/
@Slf4j
@Repository
@RequiredArgsConstructor
public class ${table.className}DaoImpl <#if !config.generateBaseDao> implements ${table.className}Dao { <#else> extends BaseDao<${table.className}, ${table.primaryKey.javaType}> implements ${table.className}Dao {</#if>


    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    protected String getTableName() {
        return "${table.tableName}";
    }

    @Override
    protected String getIdColumnName() {
        return "${table.primaryKey.columnName}";
    }

    @Override
    protected Class<${table.className}> getEntityClass() {
        return ${table.className}.class;
    }

    @Override
    protected List<String> getColumnNames() {
        return Arrays.asList(
        <#list table.columns as column>
            <#if !column.primaryKey>
                "${column.columnName}"<#if column_has_next>,</#if>
            </#if>
        </#list>
        );
    }

    @Override
    public List<${table.className}> findBy${table.columns[1].propertyName?cap_first}(${table.columns[1].javaType} ${table.columns[1].propertyName}) {
        String sql = "SELECT * FROM ${table.tableName} WHERE ${table.columns[1].columnName} = ? ORDER BY ${table.primaryKey.columnName} DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(${table.className}.class), ${table.columns[1].propertyName});
    }

    @Override
    public List<${table.className}> findBy${table.columns[1].propertyName?cap_first}(${table.columns[1].javaType} ${table.columns[1].propertyName}, int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        String sql = "SELECT * FROM ${table.tableName} WHERE ${table.columns[1].columnName} = ? ORDER BY ${table.primaryKey.columnName} DESC LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(${table.className}.class), ${table.columns[1].propertyName}, pageSize, offset);
    }

    @Override
    public long countBy${table.columns[1].propertyName?cap_first}(${table.columns[1].javaType} ${table.columns[1].propertyName}) {
        String sql = "SELECT COUNT(*) FROM ${table.tableName} WHERE ${table.columns[1].columnName} = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, ${table.columns[1].propertyName});
    }

<#if table.hasStatusColumn>
    @Override
    public List<${table.className}> findByStatus(Integer status) {
        String sql = "SELECT * FROM ${table.tableName} WHERE status = ? ORDER BY ${table.primaryKey.columnName} DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(${table.className}.class), status);
    }

    @Override
    public int updateStatus(${table.primaryKey.javaType} id, Integer status) {
        String sql = "UPDATE ${table.tableName} SET status = ? WHERE ${table.primaryKey.columnName} = ?";
        return jdbcTemplate.update(sql, status, id);
    }
</#if>

}