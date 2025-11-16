package com.small.rose.web.controller;

import com.small.rose.core.base.Result;
import com.small.rose.core.bean.GeneratorConfig;
import com.small.rose.web.service.PreviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Project: code-generator
 * @Author: 张小菜
 * @Description: [ PreviewController ] 说明： 无
 * @Function: 功能描述： 无
 * @Date: 2025/11/17 017 0:25
 * @Version: v1.0
 */

@Slf4j
@RestController
@RequestMapping("/api/preview")
@RequiredArgsConstructor
public class PreviewController {

    private final PreviewService previewService;

    /**
     * 预览单表代码 - 保持原有Result响应结构
     */
    @PostMapping("/table/{tableName}")
    public Result<Map<String, String>> previewTable(
            @RequestBody GeneratorConfig config,
            @PathVariable String tableName) {
        try {
            Map<String, String> previews = previewService.previewTableCode(config, tableName);
            return Result.success(previews);
        } catch (Exception e) {
            log.error("预览表 {} 代码失败", tableName, e);
            return Result.error("预览代码失败: " + e.getMessage());
        }
    }

    /**
     * 预览所有表代码 - 保持原有Result响应结构
     */
    @PostMapping("/tables/all")
    public Result<Map<String, Map<String, String>>> previewAllTables(@RequestBody GeneratorConfig config) {
        try {
            Map<String, Map<String, String>> allPreviews = previewService.previewAllTables(config);
            return Result.success(allPreviews);
        } catch (Exception e) {
            log.error("预览所有表代码失败", e);
            return Result.error("预览所有表失败: " + e.getMessage());
        }
    }

    /**
     * 预览配置文件 - 保持原有Result响应结构
     */
    @PostMapping("/config")
    public Result<Map<String, String>> previewConfig(@RequestBody GeneratorConfig config) {
        try {
            Map<String, String> configPreviews = previewService.previewConfigFiles(config);
            return Result.success(configPreviews);
        } catch (Exception e) {
            log.error("预览配置文件失败", e);
            return Result.error("预览配置文件失败: " + e.getMessage());
        }
    }
}
