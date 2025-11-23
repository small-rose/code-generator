package ${config.packageName}.service;

import ${config.packageName}.entity.${table.className};
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

/**
* ${table.tableComment!table.tableName} Service接口
*
* @author ${config.author}
* @since ${.now?string("yyyy-MM-dd")}
*/
public interface ${table.className}Service {

    /**
    * 根据ID查询
    */
    Optional<${table.className}> findById(${table.primaryKey.javaType} id);

    /**
    * 查询所有
    */
    List<${table.className}> findAll();

    /**
    * 分页查询
    */
    Page<${table.className}> findAll(Pageable pageable);

    /**
    * 条件查询
    */
    List<${table.className}> findAll(Specification<${table.className}> spec);

    /**
    * 条件分页查询
    */
    Page<${table.className}> findAll(Specification<${table.className}> spec, Pageable pageable);

    /**
    * 保存
    */
    ${table.className} save(${table.className} entity);

    /**
    * 批量保存
    */
    List<${table.className}> saveAll(List<${table.className}> entities);

    /**
    * 删除
    */
    void deleteById(${table.primaryKey.javaType} id);

    /**
    * 批量删除
    */
    void deleteAll(List<${table.primaryKey.javaType}> ids);

    /**
    * 判断是否存在
    */
    boolean existsById(${table.primaryKey.javaType} id);

    /**
    * 统计数量
    */
    long count();

<#-- 自定义业务方法 -->
<#list table.columns as column>
    <#if column.propertyName?contains("name") || column.propertyName?contains("title")>
    /**
     * 根据${column.columnComment!column.columnName}查询
     */
    List<${table.className}> findBy${column.propertyName?cap_first}(String ${column.propertyName});
    </#if>
</#list>

}