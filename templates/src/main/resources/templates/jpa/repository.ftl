package ${config.packageName}.repository;

import ${config.packageName}.entity.${table.className};
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
* ${table.tableComment!table.tableName} Repository接口
*
* @author ${config.author}
* @version ${config.version}
* @date ${.now?string("yyyy-MM-dd")}
*/
@Repository
public interface ${table.className}Repository extends JpaRepository<${table.className}, ${table.primaryKey.javaType}>, JpaSpecificationExecutor<${table.className}> {

/**
* 根据名称查询
*/
List<${table.className}> findBy${table.primaryKey.propertyName?cap_first}(${table.primaryKey.javaType} ${table.primaryKey.propertyName});

/**
* 根据状态查询
*/
List<${table.className}> findByStatus(Integer status);

/**
* 统计数量
*/
long countByDeletedFalse();
}