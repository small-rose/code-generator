package ${config.packageName}.dao.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * 基础DAO接口
 *
 * @param <T> 实体类型
 * @param <ID> 主键类型
 *
 * @author ${config.author}
 * @since ${.now?string("yyyy-MM-dd")}
 */
 @NoRepositoryBean
 public interface BaseDao<T, ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

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