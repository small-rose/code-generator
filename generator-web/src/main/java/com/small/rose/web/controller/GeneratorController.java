package com.small.rose.web.controller;

import com.small.rose.core.base.Result;
import com.small.rose.core.bean.DataSourceConfig;
import com.small.rose.core.bean.GeneratorConfig;
import com.small.rose.core.bean.GeneratorRequest;
import com.small.rose.core.bean.TableMeta;
import com.small.rose.core.service.CodeGenerator;
import com.small.rose.core.service.MetadataExtractor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Project: code-generator
 * @Author: 张小菜
 * @Description: [ GeneratorController ] 说明： 无
 * @Function: 功能描述： 无
 * @Date: 2025/11/16 016 23:02
 * @Version: v1.0
 */

@Slf4j
@RestController
@RequestMapping("/api/generator")
@RequiredArgsConstructor
public class GeneratorController {


    private final CodeGenerator codeGenerator;
    private final MetadataExtractor metadataExtractor;

    @PostMapping("/test-connection")
    public Result<Boolean> testConnection(@RequestBody DataSourceConfig config) {
        try {
            metadataExtractor.testConnection(config);
            return Result.success(true);
        } catch (Exception e) {
            return Result.error("连接失败: " + e.getMessage());
        }
    }

    @GetMapping("/tables")
    public Result<List<TableMeta>> getTables(DataSourceConfig config) {
        List<TableMeta> tables = metadataExtractor.extractTables(config);
        return Result.success(tables);
    }
    /**
     * 生成单表代码
     */
    @PostMapping("/generate/{tableName}")
    public Result<String> generateTable(
            @RequestBody GeneratorConfig config,
            @PathVariable String tableName) {
        try {
            codeGenerator.generateTable(config, tableName);
            return Result.success("表 " + tableName + " 代码生成成功");
        } catch (Exception e) {
            log.error("生成表 {} 代码失败", tableName, e);
            return Result.error("生成表代码失败: " + e.getMessage());
        }
    }

    @PostMapping("/generateProject")
    public Result<String> generateProject(@RequestBody GeneratorRequest request) {
        try {
            codeGenerator.generateProject(request.getConfig());
            return Result.success("代码生成成功");
        } catch (Exception e) {
            return Result.error("生成失败: " + e.getMessage());
        }
    }


}
