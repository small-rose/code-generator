package com.small.rose.core.bean;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @Project: code-generator
 * @Author: 张小菜
 * @Description: [ GeneratorConfig ] 说明： 无
 * @Function: 功能描述： 无
 * @Date: 2025/11/16 016 22:58
 * @Version: v1.0
 */
@Data
public class GeneratorConfig {

    // 数据源配置
    private String url;
    private String username;
    private String password;
    private String driverClassName;
    private DataSourceConfig dataSourceConfig;

    // 项目配置
    private String projectName = "generated-project";
    private String packageName = "com.example.demo";
    private String author = "Code Generator";
    private String version = "1.0.0";
    private String outputPath = "./generated-code";

    // 生成选项
    private Boolean generateEntity = true;
    private Boolean generateMapper = true;
    private Boolean generateService = true;
    private Boolean generateController = true;
    private Boolean generateFrontend = true;
    private Boolean generateSwagger = true;
    private Boolean generateLombok = true;

    // 技术栈选择
    private String ormFramework = "mybatis-plus"; // mybatis-plus, jpa, jdbc
    private String templateEngine = "freemarker";  // freemarker, velocity
    private String frontendFramework = "vue3";     // vue3, react

    // 自定义配置
    private Map<String, Object> customConfig = new HashMap<>();

    public String getBasePackagePath() {
        return packageName.replace(".", "/");
    }


}
