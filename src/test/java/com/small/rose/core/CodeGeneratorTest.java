package com.small.rose.core;

import com.small.rose.core.base.enums.EnumORMType;
import com.small.rose.core.bean.DataSourceConfig;
import com.small.rose.core.bean.GeneratorConfig;
import com.small.rose.core.service.CodeGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Project: code-generator
 * @Author: 张小菜
 * @Description: [ CodeGeneratorTest ] 说明： 无
 * @Function: 功能描述： 无
 * @Date: 2025/11/16 016 23:20
 * @Version: v1.0
 */
@SpringBootTest
@RunWith( SpringRunner.class)
public class CodeGeneratorTest {

    @Autowired
    private CodeGenerator codeGenerator;

    @Test
    public void testGenerateProject() {
        GeneratorConfig config = new GeneratorConfig();
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl("jdbc:h2:file:./data/demo");
        dataSourceConfig.setUsername("sa");
        dataSourceConfig.setPassword("123456");
        dataSourceConfig.setDriverClassName("org.h2.Driver");
        config.setDataSourceConfig(dataSourceConfig);
        config.setUsername(dataSourceConfig.getUsername());
        config.setPassword(dataSourceConfig.getPassword());
        config.setUrl(dataSourceConfig.getUrl());
        config.setDriverClassName(dataSourceConfig.getDriverClassName());
        config.setProjectName("demo-project");
        config.setPackageName("com.example.demo");
        config.setAuthor("Code Generator");
        config.setOutputPath("./generated-code");

        // 只生成实体类进行测试
        config.setGenerateEntity(true);
        config.setOrmFramework(EnumORMType.JPA.getCode());
        config.setGenerateMapper(true);
        config.setGenerateService(true);
        config.setGenerateController(true);
        config.setGenerateFrontend(false);

        // 生成项目
        codeGenerator.generateProject(config);
    }

    @Test
    public void testGenerateSingleTable() {
        GeneratorConfig config = new GeneratorConfig();

        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl("jdbc:h2:file:./data/demo");
        dataSourceConfig.setUsername("sa");
        dataSourceConfig.setPassword("123456");
        dataSourceConfig.setDriverClassName("org.h2.Driver");

        config.setUsername(dataSourceConfig.getUsername());
        config.setPassword(dataSourceConfig.getPassword());
        config.setUrl(dataSourceConfig.getUrl());
        config.setDriverClassName(dataSourceConfig.getDriverClassName());

        config.setProjectName("demo-project");
        config.setPackageName("com.example.demo");
        config.setOutputPath("./generated-code");

        // 生成单表
        codeGenerator.generateTable(config, "T_A1");
    }
}
