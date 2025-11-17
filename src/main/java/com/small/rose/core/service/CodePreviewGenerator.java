package com.small.rose.core.service;

import com.small.rose.core.bean.GeneratorConfig;
import com.small.rose.core.bean.TableMeta;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Project: code-generator
 * @Author: 张小菜
 * @Description: [ CodeGenerator ] 说明： 无
 * @Function: 功能描述： 无
 * @Date: 2025/11/16 016 23:00
 * @Version: v1.0
 */
@Slf4j
@AllArgsConstructor
@Component
public class CodePreviewGenerator {

    private final MetadataExtractor metadataExtractor;
    private final TemplateProcessor templateProcessor;
    private final FileWriter fileWriter;

    // 原有的生成方法保持不变...

    /**
     * 预览单表代码
     */
    public Map<String, String> previewCode(TableMeta table, GeneratorConfig config) {
        Map<String, String> previews = new LinkedHashMap<>();

        try {
            log.info("开始预览表 {} 的代码", table.getTableName());

            // 预览实体类
            if (config.getGenerateEntity()) {
                String entityCode = previewEntity(table, config);
                previews.put("Entity", entityCode);
            }

            // 预览Mapper
            if (config.getGenerateMapper()) {
                String mapperCode = previewMapper(table, config);
                previews.put("Mapper", mapperCode);

                // 预览XML（如果是MyBatis）
                if ("mybatis-plus".equals(config.getOrmFramework())) {
                    String xmlCode = previewMapperXml(table, config);
                    previews.put("MapperXML", xmlCode);
                }
            }

            // 预览Service
            if (config.getGenerateService()) {
                String serviceCode = previewService(table, config);
                previews.put("Service", serviceCode);

                String serviceImplCode = previewServiceImpl(table, config);
                previews.put("ServiceImpl", serviceImplCode);
            }

            // 预览Controller
            if (config.getGenerateController()) {
                String controllerCode = previewController(table, config);
                previews.put("Controller", controllerCode);
            }

            // 预览前端代码
            if (config.getGenerateFrontend()) {
                String vueCode = previewVuePage(table, config);
                previews.put("VuePage", vueCode);

                String apiCode = previewApi(table, config);
                previews.put("Api", apiCode);
            }

            log.info("表 {} 代码预览完成，生成 {} 个文件", table.getTableName(), previews.size());

        } catch (Exception e) {
            log.error("预览表 {} 代码失败", table.getTableName(), e);
            throw new RuntimeException("代码预览失败: " + e.getMessage(), e);
        }

        return previews;
    }

    /**
     * 预览所有表代码
     */
    public Map<String, Map<String, String>> previewAllTables(GeneratorConfig config) {
        Map<String, Map<String, String>> allPreviews = new LinkedHashMap<>();

        try {
            log.info("开始预览所有表代码");

            // 提取所有表
            List<TableMeta> tables = metadataExtractor.extractTables(config.getDataSourceConfig());

            for (TableMeta table : tables) {
                Map<String, String> tablePreviews = previewCode(table, config);
                allPreviews.put(table.getTableName(), tablePreviews);
            }

            // 添加配置文件预览
            Map<String, String> configPreviews = previewConfigFiles(config);
            allPreviews.put("Configuration", configPreviews);

            log.info("所有表代码预览完成，共 {} 张表", allPreviews.size());

        } catch (Exception e) {
            log.error("预览所有表失败", e);
            throw new RuntimeException("预览所有表失败: " + e.getMessage(), e);
        }

        return allPreviews;
    }

    /**
     * 预览配置文件
     */
    public Map<String, String> previewConfigFiles(GeneratorConfig config) {
        Map<String, String> configPreviews = new LinkedHashMap<>();

        try {
            log.info("开始预览配置文件");

            // 预览pom.xml
            String pomContent = templateProcessor.process("pom", config, config);
            configPreviews.put("pom.xml", pomContent);

            // 预览application.yml
            String appContent = templateProcessor.process("application", config, config);
            configPreviews.put("application.yml", appContent);

            // 预览启动类
            String appClassContent = templateProcessor.process("application-class", config, config);
            configPreviews.put("Application.java", appClassContent);

            log.info("配置文件预览完成，生成 {} 个文件", configPreviews.size());

        } catch (Exception e) {
            log.error("预览配置文件失败", e);
            configPreviews.put("Error", "预览配置文件失败: " + e.getMessage());
        }

        return configPreviews;
    }

    // 以下是各个文件类型的预览方法
    private String previewEntity(TableMeta table, GeneratorConfig config) {
        try {
            return templateProcessor.process("entity", table, config);
        } catch (Exception e) {
            log.error("预览实体类失败", e);
            return "// 生成实体类失败: " + e.getMessage();
        }
    }

    private String previewMapper(TableMeta table, GeneratorConfig config) {
        try {
            return templateProcessor.process("mapper", table, config);
        } catch (Exception e) {
            log.error("预览Mapper失败", e);
            return "// 生成Mapper失败: " + e.getMessage();
        }
    }

    private String previewMapperXml(TableMeta table, GeneratorConfig config) {
        try {
            return templateProcessor.process("mapper-xml", table, config);
        } catch (Exception e) {
            log.error("预览Mapper XML失败", e);
            return "<!-- 生成Mapper XML失败: " + e.getMessage() + " -->";
        }
    }

    private String previewService(TableMeta table, GeneratorConfig config) {
        try {
            return templateProcessor.process("service", table, config);
        } catch (Exception e) {
            log.error("预览Service失败", e);
            return "// 生成Service失败: " + e.getMessage();
        }
    }

    private String previewServiceImpl(TableMeta table, GeneratorConfig config) {
        try {
            return templateProcessor.process("service-impl", table, config);
        } catch (Exception e) {
            log.error("预览ServiceImpl失败", e);
            return "// 生成ServiceImpl失败: " + e.getMessage();
        }
    }

    private String previewController(TableMeta table, GeneratorConfig config) {
        try {
            return templateProcessor.process("controller", table, config);
        } catch (Exception e) {
            log.error("预览Controller失败", e);
            return "// 生成Controller失败: " + e.getMessage();
        }
    }

    private String previewVuePage(TableMeta table, GeneratorConfig config) {
        try {
            return templateProcessor.process("vue-page", table, config);
        } catch (Exception e) {
            log.error("预览Vue页面失败", e);
            return "<!-- 生成Vue页面失败: " + e.getMessage() + " -->";
        }
    }

    private String previewApi(TableMeta table, GeneratorConfig config) {
        try {
            return templateProcessor.process("api", table, config);
        } catch (Exception e) {
            log.error("预览API失败", e);
            return "// 生成API失败: " + e.getMessage();
        }
    }

}
