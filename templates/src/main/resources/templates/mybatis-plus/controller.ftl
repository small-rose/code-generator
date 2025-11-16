package ${config.packageName}.controller;

import ${config.packageName}.entity.${table.className};
import ${config.packageName}.service.${table.className}Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
<#if config.generateSwagger>
    import io.swagger.annotations.Api;
    import io.swagger.annotations.ApiOperation;
</#if>
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
* ${table.tableComment!table.tableName} 控制器
*
* @author ${config.author}
* @version ${config.version}
* @date ${.now?string("yyyy-MM-dd")}
*/
<#if config.generateSwagger>
    @Api(tags = "${table.tableComment!table.tableName}管理")
</#if>

@RestController
@RequestMapping("/api/${table.instanceName}")
@RequiredArgsConstructor
public class ${table.className}Controller {

private final ${table.className}Service ${table.instanceName}Service;

<#if config.generateSwagger>
    @ApiOperation("根据ID查询")
</#if>
@GetMapping("/{id}")
public Result<${table.className}> getById(@PathVariable ${table.primaryKey.javaType} id) {
${table.className} entity = ${table.instanceName}Service.getById(id);
return Result.success(entity);
}

<#if config.generateSwagger>
    @ApiOperation("查询所有")
</#if>
@GetMapping("/list")
public Result<List<${table.className}>> listAll() {
List<${table.className}> list = ${table.instanceName}Service.listAll();
return Result.success(list);
}

<#if config.generateSwagger>
    @ApiOperation("分页查询")
</#if>
@PostMapping("/page")
public Result<Page<${table.className}>> page(@RequestBody ${table.className} entity,
@RequestParam(defaultValue = "1") int pageNum,
@RequestParam(defaultValue = "10") int pageSize) {
Page<${table.className}> page = ${table.instanceName}Service.page(entity, pageNum, pageSize);
return Result.success(page);
}

<#if config.generateSwagger>
    @ApiOperation("新增")
</#if>
@PostMapping
public Result<Boolean> save(@RequestBody ${table.className} entity) {
    boolean result = ${table.instanceName}Service.save(entity);
    return Result.success(result);
}

    <#if config.generateSwagger>
        @ApiOperation("修改")
    </#if>
    @PutMapping
    public Result<Boolean> update(@RequestBody ${table.className} entity) {
        boolean result = ${table.instanceName}Service.update(entity);
        return Result.success(result);
    }

<#if config.generateSwagger>
     @ApiOperation("删除")
</#if>
     @DeleteMapping("/{id}")
     public Result<Boolean> delete(@PathVariable ${table.primaryKey.javaType} id) {
            boolean result = ${table.instanceName}Service.delete(id);
            return Result.success(result);
     }

<#if config.generateSwagger>
     @ApiOperation("批量删除")
</#if>
     @DeleteMapping("/batch")
     public Result<Boolean> batchDelete(@RequestBody List<${table.primaryKey.javaType}> ids) {
            boolean result = ${table.instanceName}Service.batchDelete(ids);
            return Result.success(result);
     }
}