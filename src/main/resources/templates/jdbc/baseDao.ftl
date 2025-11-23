package ${config.packageName}.dao;

import ${config.packageName}.entity.${table.className};
import ${config.packageName}.dao.base.BaseDao;
import java.util.List;
import java.util.Optional;

/**
* ${table.tableComment!table.tableName} DAO接口
*
* @author ${config.author}
* @since ${.now?string("yyyy-MM-dd")}
*/
public interface ${table.className}Dao
<#if table.hasPrimaryKey && config.generateBaseDao >
extends BaseDao<${table.className}, ${table.primaryKey.javaType}> {
</#if>

    /**
    * 根据${table.columns[1].columnComment!table.columns[1].columnName}查询
    */
    List<${table.className}> findBy${table.columns[1].propertyName?cap_first}(${table.columns[1].javaType} ${table.columns[1].propertyName});

    /**
    * 根据${table.columns[1].columnComment!table.columns[1].columnName}分页查询
    */
    List<${table.className}> findBy${table.columns[1].propertyName?cap_first}(${table.columns[1].javaType} ${table.columns[1].propertyName}, int pageNum, int pageSize);

    /**
    * 根据${table.columns[1].columnComment!table.columns[1].columnName}统计
    */
    long countBy${table.columns[1].propertyName?cap_first}(${table.columns[1].javaType} ${table.columns[1].propertyName});

<#if table.hasStatusColumn>
    /**
    * 根据状态查询
    */
    List<${table.className}> findByStatus(Integer status);

    /**
    * 更新状态
    */
    int updateStatus(${table.primaryKey.javaType} id, Integer status);
</#if>

}