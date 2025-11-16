package com.small.rose.core.config;


import freemarker.template.Configuration;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.context.annotation.Bean;

import java.util.Properties;


/**
 * @Project: code-generator
 * @Author: 张小菜
 * @Description: [ TemplateConfig ] 说明： 无
 * @Function: 功能描述： 无
 * @Date: 2025/11/16 016 23:17
 * @Version: v1.0
 */
@org.springframework.context.annotation.Configuration
public class TemplateConfig {

    /**
     * FreeMarker配置 - 用于代码生成模板
     */
    @Bean(name = "freeMarkerConfig")
    public Configuration freeMarkerConfig() {
        try {
            Configuration config = new Configuration(freemarker.template.Configuration.VERSION_2_3_32);

            // 设置模板加载路径
            config.setClassForTemplateLoading(this.getClass(), "/templates");

            // 配置设置
            config.setDefaultEncoding("UTF-8");
            config.setTemplateExceptionHandler(freemarker.template.TemplateExceptionHandler.RETHROW_HANDLER);
            config.setLogTemplateExceptions(false);
            config.setWrapUncheckedExceptions(true);
            config.setFallbackOnNullLoopVariable(false);

            // 数字格式
            config.setNumberFormat("0.##");

            return config;
        } catch (Exception e) {
            throw new RuntimeException("FreeMarker配置失败", e);
        }
    }

    /**
     * Velocity配置 - 作为备选模板引擎
     */
    @Bean(name = "velocityEngine")
    public VelocityEngine velocityEngine() {
        try {
            VelocityEngine engine = new VelocityEngine();

            Properties props = new Properties();
            props.setProperty(RuntimeConstants.RESOURCE_LOADERS, "class");
            props.setProperty("resource.loader.class.class", ClasspathResourceLoader.class.getName());
            props.setProperty(RuntimeConstants.INPUT_ENCODING, "UTF-8");
            //props.setProperty(RuntimeConstants, "UTF-8");
            //props.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,       "org.apache.velocity.runtime.log.Log4JLogChute");
            props.setProperty("runtime.log.logsystem.log4j.logger", "velocity");

            engine.init(props);
            return engine;
        } catch (Exception e) {
            // throw new RuntimeException("Velocity配置失败", e);
           return createFallbackVelocityEngine();
        }
    }

    /**
     * 备选Velocity配置
     */
    private VelocityEngine createFallbackVelocityEngine() {
        try {
            VelocityEngine engine = new VelocityEngine();
            // 最简单的配置
            engine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
            engine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
            engine.setProperty(RuntimeConstants.INPUT_ENCODING, "UTF-8");
            engine.init();
            return engine;
        } catch (Exception e) {
            throw new RuntimeException("Velocity引擎创建失败", e);
        }
    }

    /**
     * 专门用于前端模板的FreeMarker配置
     */
    @Bean(name = "frontendFreeMarkerConfig")
    public Configuration frontendFreeMarkerConfig() {
        try {
            Configuration config = new Configuration(freemarker.template.Configuration.VERSION_2_3_32);

            // 前端模板放在不同的目录
            config.setClassForTemplateLoading(this.getClass(), "/templates/frontend");

            // 前端模板的特殊配置
            config.setDefaultEncoding("UTF-8");
            config.setTemplateExceptionHandler(freemarker.template.TemplateExceptionHandler.IGNORE_HANDLER);
            config.setWhitespaceStripping(true);

            return config;
        } catch (Exception e) {
            throw new RuntimeException("前端FreeMarker配置失败", e);
        }
    }


}
