package com.small.rose.core.base.dao.jdbc;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Project: code-generator
 * @Author: 张小菜
 * @Description: [ JdbcBaseDao ] 接口说明：
 * 基础DAO实现类 - 基于JdbcTemplate
 *
 * @param <T> 实体类型
 * @param <ID> 主键类型
 * @Function: 功能描述： 无
 * @Date: 2025/11/23 023 22:52
 * @Version: v1.0
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public abstract class JdbcBaseDaoImpl<T, ID> implements JdbcBaseDao<T, ID>{


    protected final JdbcTemplate jdbcTemplate;
    protected final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * 获取表名
     */
    protected abstract String getTableName();

    /**
     * 获取主键列名
     */
    protected abstract String getIdColumnName();

    /**
     * 获取实体类类型
     */
    protected abstract Class<T> getEntityClass();

    /**
     * 获取所有列名（排除主键）
     */
    protected abstract List<String> getColumnNames();

    /**
     * 获取所有列名（包含主键）
     */
    protected List<String> getAllColumnNames() {
        List<String> columns = new ArrayList<>(getColumnNames());
        columns.add(0, getIdColumnName());
        return columns;
    }

    @Override
    public Optional<T> findById(ID id) {
        String sql = String.format("SELECT * FROM %s WHERE %s = ?", getTableName(), getIdColumnName());
        try {
            T entity = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(getEntityClass()), id);
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            log.warn("根据ID查询失败: {}, id: {}", sql, id);
            return Optional.empty();
        }
    }

    @Override
    public List<T> findAll() {
        String sql = String.format("SELECT * FROM %s ORDER BY %s DESC", getTableName(), getIdColumnName());
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(getEntityClass()));
    }

    @Override
    public List<T> findPage(int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        String sql = String.format("SELECT * FROM %s ORDER BY %s DESC LIMIT ? OFFSET ?",
                getTableName(), getIdColumnName());
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(getEntityClass()), pageSize, offset);
    }

    @Override
    public List<T> findByCondition(Map<String, Object> condition) {
        if (condition == null || condition.isEmpty()) {
            return findAll();
        }

        StringBuilder sql = new StringBuilder("SELECT * FROM ").append(getTableName()).append(" WHERE 1=1");
        MapSqlParameterSource params = new MapSqlParameterSource();

        condition.forEach((key, value) -> {
            sql.append(" AND ").append(key).append(" = :").append(key);
            params.addValue(key, value);
        });

        sql.append(" ORDER BY ").append(getIdColumnName()).append(" DESC");

        return namedParameterJdbcTemplate.query(sql.toString(), params, new BeanPropertyRowMapper<>(getEntityClass()));
    }

    @Override
    public List<T> findByCondition(Map<String, Object> condition, int pageNum, int pageSize) {
        if (condition == null || condition.isEmpty()) {
            return findPage(pageNum, pageSize);
        }

        StringBuilder sql = new StringBuilder("SELECT * FROM ").append(getTableName()).append(" WHERE 1=1");
        MapSqlParameterSource params = new MapSqlParameterSource();

        condition.forEach((key, value) -> {
            sql.append(" AND ").append(key).append(" = :").append(key);
            params.addValue(key, value);
        });

        int offset = (pageNum - 1) * pageSize;
        sql.append(" ORDER BY ").append(getIdColumnName()).append(" DESC LIMIT ").append(pageSize)
                .append(" OFFSET ").append(offset);

        return namedParameterJdbcTemplate.query(sql.toString(), params, new BeanPropertyRowMapper<>(getEntityClass()));
    }

    @Override
    public long count() {
        String sql = String.format("SELECT COUNT(*) FROM %s", getTableName());
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    @Override
    public long countByCondition(Map<String, Object> condition) {
        if (condition == null || condition.isEmpty()) {
            return count();
        }

        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM ").append(getTableName()).append(" WHERE 1=1");
        MapSqlParameterSource params = new MapSqlParameterSource();

        condition.forEach((key, value) -> {
            sql.append(" AND ").append(key).append(" = :").append(key);
            params.addValue(key, value);
        });

        return namedParameterJdbcTemplate.queryForObject(sql.toString(), params, Long.class);
    }

    @Override
    public int save(T entity) {
        List<String> columns = getColumnNames();
        String columnNames = String.join(", ", columns);
        String placeholders = columns.stream().map(col -> ":" + col).collect(Collectors.joining(", "));

        String sql = String.format("INSERT INTO %s (%s) VALUES (%s)",
                getTableName(), columnNames, placeholders);

        SqlParameterSource paramSource = new BeanPropertySqlParameterSource(entity);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        int result = namedParameterJdbcTemplate.update(sql, paramSource, keyHolder);

        // 设置自增主键
        if (keyHolder.getKey() != null) {
            try {
                Field idField = getEntityClass().getDeclaredField(getIdColumnName());
                idField.setAccessible(true);
                idField.set(entity, keyHolder.getKey().longValue());
            } catch (Exception e) {
                log.warn("设置自增主键失败", e);
            }
        }

        return result;
    }

    @Override
    public int[][] batchSave(List<T> entities) {
        if (entities == null || entities.isEmpty()) {
            return new int[0][0];
        }

        List<String> columns = getColumnNames();
        String columnNames = String.join(", ", columns);
        String placeholders = columns.stream().map(col -> "?").collect(Collectors.joining(", "));

        String sql = String.format("INSERT INTO %s (%s) VALUES (%s)",
                getTableName(), columnNames, placeholders);

        return jdbcTemplate.batchUpdate(sql, entities, entities.size(), (ps, entity) -> {
            try {
                for (int i = 0; i < columns.size(); i++) {
                    Field field = getEntityClass().getDeclaredField(columns.get(i));
                    field.setAccessible(true);
                    ps.setObject(i + 1, field.get(entity));
                }
            } catch (Exception e) {
                throw new RuntimeException("批量插入参数设置失败", e);
            }
        });
    }

    @Override
    public int update(T entity) {
        List<String> columns = getColumnNames();
        String setClause = columns.stream()
                .map(col -> col + " = :" + col)
                .collect(Collectors.joining(", "));

        String sql = String.format("UPDATE %s SET %s WHERE %s = :%s",
                getTableName(), setClause, getIdColumnName(), getIdColumnName());

        SqlParameterSource paramSource = new BeanPropertySqlParameterSource(entity);
        return namedParameterJdbcTemplate.update(sql, paramSource);
    }

    @Override
    public int updateById(ID id, Map<String, Object> updateFields) {
        if (updateFields == null || updateFields.isEmpty()) {
            return 0;
        }

        String setClause = updateFields.keySet().stream()
                .map(key -> key + " = :" + key)
                .collect(Collectors.joining(", "));

        String sql = String.format("UPDATE %s SET %s WHERE %s = :id",
                getTableName(), setClause, getIdColumnName());

        MapSqlParameterSource params = new MapSqlParameterSource(updateFields);
        params.addValue("id", id);

        return namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    public int deleteById(ID id) {
        String sql = String.format("DELETE FROM %s WHERE %s = ?", getTableName(), getIdColumnName());
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public int[][] batchDeleteByIds(List<ID> ids) {
        String sql = String.format("DELETE FROM %s WHERE %s = ?", getTableName(), getIdColumnName());
        return jdbcTemplate.batchUpdate(sql, ids, ids.size(), (ps, id) -> ps.setObject(1, id));
    }

    @Override
    public int deleteByCondition(Map<String, Object> condition) {
        if (condition == null || condition.isEmpty()) {
            return 0;
        }

        StringBuilder sql = new StringBuilder("DELETE FROM ").append(getTableName()).append(" WHERE 1=1");
        MapSqlParameterSource params = new MapSqlParameterSource();

        condition.forEach((key, value) -> {
            sql.append(" AND ").append(key).append(" = :").append(key);
            params.addValue(key, value);
        });

        return namedParameterJdbcTemplate.update(sql.toString(), params);
    }

    @Override
    public boolean existsById(ID id) {
        String sql = String.format("SELECT COUNT(*) FROM %s WHERE %s = ?", getTableName(), getIdColumnName());
        Long count = jdbcTemplate.queryForObject(sql, Long.class, id);
        return count != null && count > 0;
    }

}
