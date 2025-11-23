package com.small.rose.core.base.dao.jdbc;


import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Project: code-generator
 * @Author: 张小菜
 * @Description: [ JdbcBaseDao ] 接口说明： 无
 * @Function: 功能描述： 无
 * @Date: 2025/11/23 023 22:52
 * @Version: v1.0
 */
public interface JdbcBaseDao<T, ID> {


    /**
     * 根据ID查询
     */
    Optional<T> findById(ID id);

    /**
     * 查询所有
     */
    List<T> findAll();

    /**
     * 分页查询
     */
    List<T> findPage(int pageNum, int pageSize);

    /**
     * 条件查询
     */
    List<T> findByCondition(Map<String, Object> condition);

    /**
     * 条件分页查询
     */
    List<T> findByCondition(Map<String, Object> condition, int pageNum, int pageSize);

    /**
     * 统计总数
     */
    long count();

    /**
     * 条件统计
     */
    long countByCondition(Map<String, Object> condition);

    /**
     * 保存实体
     */
    int save(T entity);

    /**
     * 批量保存
     */
    int[][] batchSave(List<T> entities);

    /**
     * 更新实体
     */
    int update(T entity);

    /**
     * 根据ID更新特定字段
     */
    int updateById(ID id, Map<String, Object> updateFields);

    /**
     * 根据ID删除
     */
    int deleteById(ID id);

    /**
     * 批量删除
     */
    int[][] batchDeleteByIds(List<ID> ids);

    /**
     * 条件删除
     */
    int deleteByCondition(Map<String, Object> condition);

    /**
     * 判断是否存在
     */
    boolean existsById(ID id);

}
