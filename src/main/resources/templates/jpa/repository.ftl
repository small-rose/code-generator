package ${config.packageName}.repository;

import ${config.packageName}.entity.${table.className};
import ${config.packageName}.dao.base.BaseDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* ${table.tableComment!table.tableName} Repository接口
*
* @author ${config.author}
* @since ${.now?string("yyyy-MM-dd")}
*/
@Repository
public interface ${table.className}Repository
<#if table.hasPrimaryKey && config.generateBaseDao>
extends BaseDao<${table.className}, ${table.primaryKey.javaType}> {
</#if>
<#-- 自定义查询方法 -->
<#list table.columns as column>
    <#if column.propertyName?contains("name") || column.propertyName?contains("title")>
        /**
        * 根据${column.columnComment!column.columnName}查询
        */
        List<${table.className}> findBy${column.propertyName?cap_first}(String ${column.propertyName});

        /**
        * 根据${column.columnComment!column.columnName}模糊查询
        */
        List<${table.className}> findBy${column.propertyName?cap_first}Containing(String ${column.propertyName});

        /**
        * 根据${column.columnComment!column.columnName}查询并分页
        */
        Page<${table.className}> findBy${column.propertyName?cap_first}Containing(String ${column.propertyName}, Pageable pageable);
    </#if>
</#list>

    /**
    * 自定义查询示例
    */
    @Query("SELECT e FROM ${table.className} e WHERE e.createTime > :startTime")
    List<${table.className}> findRecent(@Param("startTime") LocalDateTime startTime);

    /**
    * 统计查询
    */
    @Query("SELECT COUNT(e) FROM ${table.className} e WHERE e.deleted = false")
    long countActive();
}