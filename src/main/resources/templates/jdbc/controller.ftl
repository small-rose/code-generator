package ${config.packageName}.controller;

import ${config.packageName}.entity.${table.className};
import ${config.packageName}.service.${table.className}Service;
import lombok.RequiredArgsConstructor;
<#if config.generateSwagger>
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
</#if>
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * ${table.tableComment!table.tableName} Controller
 *
 * @author ${config.author}
 * @since ${.now?string("yyyy-MM-dd")}
 */
<#if config.generateSwagger>
@Api(tags = "${table.tableComment!table.tableName}管理")
</#if>
@RestController
@RequestMapping("/api/${table.instanceName}")
@RequiredArgsConstructor
public class ${table.className}Controller {

    private final ${table.className}Service ${table.instanceName}Service;

<#if table.hasPrimaryKey && config.generateSwagger>
    @ApiOperation("根据ID查询")
</#if>
<#if table.hasPrimaryKey>
    @GetMapping("/{id}")
    public ResponseEntity<${table.className}> getById(@PathVariable ${table.primaryKey.javaType} id) {
        Optional<${table.className}> result = ${table.instanceName}Service.findById(id);
        return result.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
</#if>

<#if config.generateSwagger>
    @ApiOperation("分页查询")
</#if>
    @GetMapping("/page")
    public ResponseEntity<Page<${table.className}>> getPage(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        Page<${table.className}> result = ${table.instanceName}Service.findAll(pageable);
        return ResponseEntity.ok(result);
    }

<#if config.generateSwagger>
    @ApiOperation("新增")
</#if>
    @PostMapping
    public ResponseEntity<${table.className}> create(@RequestBody ${table.className} ${table.instanceName}) {
        ${table.className} saved = ${table.instanceName}Service.save(${table.instanceName});
        return ResponseEntity.ok(saved);
    }

<#if table.hasPrimaryKey && config.generateSwagger>
    @ApiOperation("修改")
</#if>
<#if table.hasPrimaryKey >
    @PutMapping("/{id}")
    public ResponseEntity<${table.className}> update(@PathVariable ${table.primaryKey.javaType} id, @RequestBody ${table.className} ${table.instanceName}) {
        ${table.instanceName}.setId(id);
        ${table.className} updated = ${table.instanceName}Service.save(${table.instanceName});
        return ResponseEntity.ok(updated);
    }
</#if>

<#if config.generateSwagger>
    @ApiOperation("删除")
</#if>
<#if table.hasPrimaryKey >
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable ${table.primaryKey.javaType} id) {
        ${table.instanceName}Service.deleteById(id);
        return ResponseEntity.ok().build();
    }
</#if>

}