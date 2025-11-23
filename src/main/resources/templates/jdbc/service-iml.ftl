package ${config.packageName}.service.impl;

import ${config.packageName}.entity.${table.className};
import ${config.packageName}.repository.${table.className}Repository;
import ${config.packageName}.service.${table.className}Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
* ${table.tableComment!table.tableName} Service实现类
*
* @author ${config.author}
* @since ${.now?string("yyyy-MM-dd")}
*/
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ${table.className}ServiceImpl implements ${table.className}Service {

    private final ${table.className}Repository ${table.instanceName}Repository;

    @Override
    @Transactional(readOnly = true)
    public Optional<${table.className}> findById(${table.primaryKey.javaType} id) {
        return ${table.instanceName}Repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<${table.className}> findAll() {
        return ${table.instanceName}Repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<${table.className}> findAll(Pageable pageable) {
        return ${table.instanceName}Repository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<${table.className}> findAll(Specification<${table.className}> spec) {
        return ${table.instanceName}Repository.findAll(spec);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<${table.className}> findAll(Specification<${table.className}> spec, Pageable pageable) {
        return ${table.instanceName}Repository.findAll(spec, pageable);
    }

    @Override
    public ${table.className} save(${table.className} entity) {
        return ${table.instanceName}Repository.save(entity);
    }

    @Override
    public List<${table.className}> saveAll(List<${table.className}> entities) {
        return ${table.instanceName}Repository.saveAll(entities);
    }

    @Override
    public void deleteById(${table.primaryKey.javaType} id) {
        ${table.instanceName}Repository.deleteById(id);
    }

    @Override
    public void deleteAll(List<${table.primaryKey.javaType}> ids) {
        ${table.instanceName}Repository.deleteAllById(ids);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(${table.primaryKey.javaType} id) {
        return ${table.instanceName}Repository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return ${table.instanceName}Repository.count();
    }

<#-- 自定义业务方法实现 -->
<#list table.columns as column>
   <#if column.propertyName?contains("name") || column.propertyName?contains("title")>
   @Override
   @Transactional(readOnly = true)
   public List<${table.className}> findBy${column.propertyName?cap_first}(String ${column.propertyName}) {
       return ${table.instanceName}Repository.findBy${column.propertyName?cap_first}(${column.propertyName});
   }
   </#if>
</#list>

    /**
    * 构建查询条件示例
    */
    public Specification<${table.className}> buildSpec(${table.className} query) {
    return (root, criteriaQuery, criteriaBuilder) -> {
        List<Predicate> predicates = new ArrayList<>();

            <#list table.columns as column>
                <#if column.propertyName?contains("name") || column.propertyName?contains("title")>
                    if (StringUtils.isNotBlank(query.get${column.propertyName?cap_first}())) {
                    predicates.add(criteriaBuilder.like(root.get("${column.propertyName}"),
                    "%" + query.get${column.propertyName?cap_first}() + "%"));
                    }
                </#if>
            </#list>

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
         };
    }
}