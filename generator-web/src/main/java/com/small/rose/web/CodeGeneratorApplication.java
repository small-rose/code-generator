package com.small.rose.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Project: code-generator
 * @Author: 张小菜
 * @Description: [ CodeGeneratorApplication ] 说明： 无
 * @Function: 功能描述： 无
 * @Date: 2025/11/16 016 22:52
 * @Version: v1.0
 */
@SpringBootApplication
public class CodeGeneratorApplication {

    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(CodeGeneratorApplication.class, args);
        System.out.println("code generator 平台启动成功 ^_^");
    }
}
