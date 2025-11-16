package com.small.rose.core.service;

import com.small.rose.core.bean.GeneratorConfig;
import com.small.rose.core.bean.TableMeta;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

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
public class CodeGenerator {

    private final MetadataExtractor metadataExtractor;
    private final TemplateProcessor templateProcessor;
    private final FileWriter fileWriter;

    /**
     * 生成完整项目
     */
    public void generateProject(GeneratorConfig config) {
        try {
            log.info("开始生成项目: {}", config.getProjectName());

            // 1. 创建项目结构
            createProjectStructure(config);

            // 2. 提取数据库表
            List<TableMeta> tables = metadataExtractor.extractTables(config.getDataSourceConfig());
            log.info("提取到 {} 张表", tables.size());

            // 3. 生成每张表的代码
            for (TableMeta table : tables) {
                generateTableCode(table, config);
            }

            // 4. 生成配置文件
            generateConfigFiles(config, tables);

            log.info("项目生成完成: {}", config.getOutputPath());

        } catch (Exception e) {
            log.error("生成项目失败", e);
            throw new RuntimeException("代码生成失败: " + e.getMessage(), e);
        }
    }

    /**
     * 生成单表代码
     */
    public void generateTable(GeneratorConfig config, String tableName) {
        try {
            TableMeta table = metadataExtractor.extractTable(config.getDataSourceConfig(), tableName);
            generateTableCode(table, config);
            log.info("表 {} 代码生成完成", tableName);
        } catch (Exception e) {
            log.error("生成表 {} 代码失败", tableName, e);
            throw new RuntimeException("生成表代码失败: " + tableName, e);
        }
    }

    /**
     * 创建项目目录结构
     */
    private void createProjectStructure(GeneratorConfig config) {
        String basePath = config.getOutputPath() + File.separator + config.getProjectName();

        // 创建标准Maven目录
        createDirectory(basePath + "/src/main/java");
        createDirectory(basePath + "/src/main/resources");
        createDirectory(basePath + "/src/test/java");

        if (config.getGenerateFrontend()) {
            createDirectory(basePath + "/src/main/frontend/src");
        }

        log.info("项目结构创建完成: {}", basePath);
    }

    /**
     * 生成单表代码
     */
    private void generateTableCode(TableMeta table, GeneratorConfig config) {
        log.info("生成表 {} 的代码", table.getTableName());

        try {
            if (config.getGenerateEntity()) {
                generateEntity(table, config);
            }
            if (config.getGenerateMapper()) {
                generateMapper(table, config);
            }
            if (config.getGenerateService()) {
                generateService(table, config);
            }
            if (config.getGenerateController()) {
                generateController(table, config);
            }
            if (config.getGenerateFrontend()) {
                generateFrontend(table, config);
            }
        } catch (Exception e) {
            log.error("生成表 {} 代码失败", table.getTableName(), e);
            throw new RuntimeException("生成表代码失败: " + table.getTableName(), e);
        }
    }

    private void generateEntity(TableMeta table, GeneratorConfig config) {
        String content = templateProcessor.process("entity", table, config);
        String filePath = buildFilePath(config, "entity", table.getClassName() + ".java");
        fileWriter.write(filePath, content);
    }

    private void generateMapper(TableMeta table, GeneratorConfig config) {
        String content = templateProcessor.process("mapper", table, config);
        String filePath = buildFilePath(config, "mapper", table.getClassName() + "Mapper.java");
        fileWriter.write(filePath, content);
    }

    private void generateService(TableMeta table, GeneratorConfig config) {
        String interfaceContent = templateProcessor.process("service", table, config);
        String interfacePath = buildFilePath(config, "service", table.getClassName() + "Service.java");
        fileWriter.write(interfacePath, interfaceContent);

        String implContent = templateProcessor.process("service-impl", table, config);
        String implPath = buildFilePath(config, "service/impl", table.getClassName() + "ServiceImpl.java");
        fileWriter.write(implPath, implContent);
    }

    private void generateController(TableMeta table, GeneratorConfig config) {
        String content = templateProcessor.process("controller", table, config);
        String filePath = buildFilePath(config, "controller", table.getClassName() + "Controller.java");
        fileWriter.write(filePath, content);
    }

    private void generateFrontend(TableMeta table, GeneratorConfig config) {
        if (!config.getGenerateFrontend()) return;

        String vueContent = templateProcessor.process("vue-page", table, config);
        String vuePath = buildFrontendPath(config, "views", table.getClassName() + "List.vue");
        fileWriter.write(vuePath, vueContent);

        String apiContent = templateProcessor.process("api", table, config);
        String apiPath = buildFrontendPath(config, "api", table.getClassName() + "Api.js");
        fileWriter.write(apiPath, apiContent);
    }

    private void generateConfigFiles(GeneratorConfig config, List<TableMeta> tables) {
        // 生成pom.xml
        String pomContent = templateProcessor.process("pom", config, config);
        String pomPath = config.getOutputPath() + File.separator + config.getProjectName() + "/pom.xml";
        fileWriter.write(pomPath, pomContent);

        // 生成application.yml
        String appContent = templateProcessor.process("application", config, config);
        String appPath = buildResourcePath(config, "", "application.yml");
        fileWriter.write(appPath, appContent);

        // 生成启动类
        String appClassContent = templateProcessor.process("application-class", config, config);
        String appClassPath = buildFilePath(config, "", "Application.java");
        fileWriter.write(appClassPath, appClassContent);
    }

    // 路径构建方法
    private String buildFilePath(GeneratorConfig config, String module, String fileName) {
        return config.getOutputPath() + File.separator + config.getProjectName() +
                "/src/main/java/" + config.getPackageName().replace(".", "/") + "/" +
                module + "/" + fileName;
    }

    private String buildResourcePath(GeneratorConfig config, String module, String fileName) {
        return config.getOutputPath() + File.separator + config.getProjectName() +
                "/src/main/resources/" + module + "/" + fileName;
    }

    private String buildFrontendPath(GeneratorConfig config, String module, String fileName) {
        return config.getOutputPath() + File.separator + config.getProjectName() +
                "/src/main/frontend/src/" + module + "/" + fileName;
    }

    private void createDirectory(String path) {
        File dir = new File(path);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new RuntimeException("创建目录失败: " + path);
        }
    }
}
