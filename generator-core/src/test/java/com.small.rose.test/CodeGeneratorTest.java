package com.small.rose.test;

import com.small.rose.core.bean.GeneratorConfig;
import com.small.rose.core.service.CodeGenerator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Project: code-generator
 * @Author: 张小菜
 * @Description: [ CodeGeneratorTest ] 说明： 无
 * @Function: 功能描述： 无
 * @Date: 2025/11/16 016 23:20
 * @Version: v1.0
 */
@SpringBootTest
public class CodeGeneratorTest {

    @Autowired
    private CodeGenerator codeGenerator;

    @Test
    void testGenerateProject() {
        GeneratorConfig config = new GeneratorConfig();
        config.setUrl("jdbc:mysql://localhost:3306/test");
        config.setUsername("root");
        config.setPassword("123456");
        config.setProjectName("demo-project");
        config.setPackageName("com.example.demo");
        config.setAuthor("Code Generator");
        config.setOutputPath("./generated-code");

        // 只生成实体类进行测试
        config.setGenerateEntity(true);
        config.setGenerateMapper(false);
        config.setGenerateService(false);
        config.setGenerateController(false);
        config.setGenerateFrontend(false);

        // 生成项目
        codeGenerator.generateProject(config);
    }

    @Test
    void testGenerateSingleTable() {
        GeneratorConfig config = new GeneratorConfig();
        config.setUrl("jdbc:mysql://localhost:3306/test");
        config.setUsername("root");
        config.setPassword("123456");
        config.setProjectName("demo-project");
        config.setPackageName("com.example.demo");
        config.setOutputPath("./generated-code");

        // 生成单表
        codeGenerator.generateTable(config, "user");
    }
}
