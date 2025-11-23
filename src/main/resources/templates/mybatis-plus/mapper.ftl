package ${config.packageName}.mapper;

import ${config.packageName}.entity.${table.className};
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * ${table.tableComment!table.tableName} Mapper接口
 *
 * @author ${config.author}
 * @version ${config.version}
 * @date ${.now?string("yyyy-MM-dd")}
 */
@Mapper
public interface ${table.className}Mapper extends BaseMapper<${table.className}> {


<#if table.hasPrimaryKey>
    /**
    * 根据主键查询
    */
    ${table.className} selectByPrimaryKey(@Param("${table.primaryKey.propertyName}") ${table.primaryKey.javaType} ${table.primaryKey.propertyName});
</#if>

    /**
    * 批量插入
    */
    int batchInsert(@Param("list") List<${table.className}> list);

<#if table.hasPrimaryKey>
    /**
    * 逻辑删除
    */
    int logicDelete(@Param("${table.primaryKey.propertyName}") ${table.primaryKey.javaType} ${table.primaryKey.propertyName});
</#if>
    /**
     * 分页查询
     */
    List<${table.className}> selectPage(@Param("entity") ${table.className} entity);

}