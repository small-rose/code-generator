package com.small.rose.core.service;

import com.small.rose.core.bean.GeneratorConfig;
import com.small.rose.core.bean.TableMeta;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Project: code-generator
 * @Author: 张小菜
 * @Description: [ PreviewService ] 说明： 无
 * @Function: 功能描述： 无
 * @Date: 2025/11/17 017 0:24
 * @Version: v1.0
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class PreviewService {

    private final MetadataExtractor metadataExtractor;
    private final CodePreviewGenerator codeGenerator;

    /**
     * 预览单表代码 - 保持原有响应结构
     */
    public Map<String, String> previewTableCode(GeneratorConfig config, String tableName) {
        try {
            log.info("开始预览表 {} 的代码", tableName);

            // 提取表信息
            TableMeta table = metadataExtractor.extractTable(config.getDataSourceConfig(), tableName);

            // 使用原有的previewCode方法
            Map<String, String> previews = codeGenerator.previewCode(table, config);

            log.info("表 {} 代码预览完成，生成 {} 个文件", tableName, previews.size());
            return previews;

        } catch (Exception e) {
            log.error("预览表 {} 代码失败", tableName, e);
            throw new RuntimeException("预览代码失败: " + e.getMessage(), e);
        }
    }

    /**
     * 预览所有表代码 - 保持原有响应结构
     */
    public Map<String, Map<String, String>> previewAllTables(GeneratorConfig config) {
        try {
            log.info("开始预览所有表代码");

            // 使用原有的previewAllTables方法
            Map<String, Map<String, String>> allPreviews = codeGenerator.previewAllTables(config);

            log.info("所有表代码预览完成，共 {} 张表", allPreviews.size());
            return allPreviews;

        } catch (Exception e) {
            log.error("预览所有表代码失败", e);
            throw new RuntimeException("预览所有表失败: " + e.getMessage(), e);
        }
    }

    /**
     * 预览配置文件 - 保持原有响应结构
     */
    public Map<String, String> previewConfigFiles(GeneratorConfig config) {
        try {
            log.info("开始预览配置文件");

            // 使用原有的previewConfigFiles方法
            Map<String, String> configPreviews = codeGenerator.previewConfigFiles(config);

            log.info("配置文件预览完成，生成 {} 个文件", configPreviews.size());
            return configPreviews;

        } catch (Exception e) {
            log.error("预览配置文件失败", e);
            throw new RuntimeException("预览配置文件失败: " + e.getMessage(), e);
        }
    }

}
