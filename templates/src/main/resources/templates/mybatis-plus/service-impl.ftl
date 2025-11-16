package ${config.packageName}.service.impl;

import ${config.packageName}.entity.${table.className};
import ${config.packageName}.mapper.${table.className}Mapper;
import ${config.packageName}.service.${table.className}Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
* ${table.tableComment!table.tableName} 服务实现类
*
* @author ${config.author}
* @version ${config.version}
* @date ${.now?string("yyyy-MM-dd")}
*/
@Slf4j
@Service
@RequiredArgsConstructor
public class ${table.className}ServiceImpl extends ServiceImpl<${table.className}Mapper, ${table.className}> implements ${table.className}Service {

private final ${table.className}Mapper ${table.instanceName}Mapper;

@Override
public ${table.className} getById(${table.primaryKey.javaType} id) {
return ${table.instanceName}Mapper.selectById(id);
}

@Override
public List<${table.className}> listAll() {
return ${table.instanceName}Mapper.selectList(null);
}

@Override
public Page<${table.className}> page(${table.className} entity, int pageNum, int pageSize) {
Page<${table.className}> page = new Page<>(pageNum, pageSize);
return ${table.instanceName}Mapper.selectPage(page, entity);
}

@Override
@Transactional(rollbackFor = Exception.class)
public boolean save(${table.className} entity) {
return ${table.instanceName}Mapper.insert(entity) > 0;
}

@Override
@Transactional(rollbackFor = Exception.class)
public boolean update(${table.className} entity) {
return ${table.instanceName}Mapper.updateById(entity) > 0;
}

@Override
@Transactional(rollbackFor = Exception.class)
public boolean delete(${table.primaryKey.javaType} id) {
return ${table.instanceName}Mapper.deleteById(id) > 0;
}

@Override
@Transactional(rollbackFor = Exception.class)
public boolean batchDelete(List<${table.primaryKey.javaType}> ids) {
return ${table.instanceName}Mapper.deleteBatchIds(ids) > 0;
}

}