package ${config.packageName}.service.interfaces;

import ${config.packageName}.entity.${table.className};
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;

/**
* ${table.tableComment!table.tableName} 服务接口
*
* @author ${config.author}
* @version ${config.version}
* @date ${.now?string("yyyy-MM-dd")}
*/
public interface ${table.className}Service {

<#if table.hasPrimaryKey>
    /**
    * 根据ID查询
    */
    ${table.className} getById(${table.primaryKey.javaType} id);
</#if>

    /**
    * 查询所有
    */
    List<${table.className}> listAll();

    /**
    * 分页查询
    */
    Page<${table.className}> page(${table.className} entity, int pageNum, int pageSize);

    /**
    * 新增
    */
    boolean save(${table.className} entity);

    /**
    * 修改
    */
    boolean update(${table.className} entity);

<#if table.hasPrimaryKey>
    /**
    * 删除
    */
    boolean delete(${table.primaryKey.javaType} id);
</#if>

<#if table.hasPrimaryKey>
    /**
    * 批量删除
    */
    boolean batchDelete(List<${table.primaryKey.javaType}> ids);
</#if>

}