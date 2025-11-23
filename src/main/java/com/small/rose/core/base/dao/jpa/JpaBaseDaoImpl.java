package com.small.rose.core.base.dao.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

/**
 * @Project: code-generator
 * @Author: 张小菜
 * @Description: [ JpaBaseDaoImpl ] 说明： 无
 * @Function: 功能描述： 无
 * @Date: 2025/11/23 023 22:44
 * @Version: v1.0
 */
@Transactional(readOnly = true)
public class JpaBaseDaoImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements JpaBaseDao<T, ID> {

    private final EntityManager entityManager;
    private final JpaEntityInformation<T, ID> entityInformation;

    public JpaBaseDaoImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
        this.entityInformation = entityInformation;
    }

    @Override
    public List<T> findByIdIn(List<ID> ids) {
        return findAll((root, query, cb) -> root.get("id").in(ids));
    }

    @Override
    public Page<T> findByIdIn(List<ID> ids, Pageable pageable) {
        return findAll((root, query, cb) -> root.get("id").in(ids), pageable);
    }

    @Override
    public boolean existsById(ID id) {
        return findById(id).isPresent();
    }

    @Override
    public List<T> findAll(Sort sort) {
        return findAll((Specification<T>) null, sort);
    }

    @Override
    @Transactional
    public <S extends T> S save(S entity) {
        if (entityInformation.isNew(entity)) {
            entityManager.persist(entity);
            return entity;
        } else {
            return entityManager.merge(entity);
        }
    }

    @Override
    @Transactional
    public <S extends T> List<S> saveAll(Iterable<S> entities) {
        entities.forEach(this::save);
        return (List<S>) entities;
    }
}
