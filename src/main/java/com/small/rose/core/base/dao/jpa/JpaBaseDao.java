package com.small.rose.core.base.dao.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;


/**
 * 基础DAO接口
 *
 * @Project: code-generator
 * @Author: 张小菜
 * @Description: [ BaseDao ] 接口说明： 无
 * @Function: 功能描述： 无
 * @Date: 2025/11/23 023 22:41
 * @Version: v1.0
 */
@NoRepositoryBean
public interface JpaBaseDao<T, ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {


    /**
     * 根据ID列表查询
     */
    List<T> findByIdIn(List<ID> ids);

    /**
     * 根据ID列表查询并分页
     */
    Page<T> findByIdIn(List<ID> ids, Pageable pageable);

    /**
     * 判断是否存在
     */
    boolean existsById(ID id);

    /**
     * 查询所有（带排序）
     */
    List<T> findAll(Sort sort);

    /**
     * 条件查询
     */
    List<T> findAll(Specification<T> spec);

    /**
     * 条件查询（带排序）
     */
    List<T> findAll(Specification<T> spec, Sort sort);
}
