package com.small.rose.core.service;

import com.small.rose.core.bean.GeneratorConfig;
import com.small.rose.core.bean.TableMeta;
import com.small.rose.core.utils.TemplateUtils;
import freemarker.template.Template;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @Project: code-generator
 * @Author: 张小菜
 * @Description: [ TemplateProcessor ] 说明： 无
 * @Function: 功能描述： 无
 * @Date: 2025/11/16 016 23:14
 * @Version: v1.0
 */

@Slf4j
@AllArgsConstructor
@Component
public class TemplateProcessor {

    @Qualifier("freeMarkerConfig")
    private final freemarker.template.Configuration freeMarkerConfig;

    @Qualifier("frontendFreeMarkerConfig")
    private final freemarker.template.Configuration frontendFreeMarkerConfig;

    @Qualifier("velocityEngine")
    private final VelocityEngine velocityEngine;

    /**
     * 处理模板 - 根据配置选择模板引擎
     */
    public String process(String templateName, Object data, GeneratorConfig config) {
        try {
            String templateEngine = config.getTemplateEngine();

            switch (templateEngine.toLowerCase()) {
                case "freemarker":
                    return processWithFreeMarker(templateName, data, config);
                case "velocity":
                    return processWithVelocity(templateName, data, config);
                case "frontend":
                    return processWithFrontendTemplate(templateName, data, config);
                default:
                    // 默认使用FreeMarker
                    return processWithFreeMarker(templateName, data, config);
            }
        } catch (Exception e) {
            log.error("模板处理失败: {}", templateName, e);
            throw new RuntimeException("模板处理失败: " + templateName, e);
        }
    }

    /**
     * 使用FreeMarker处理后端代码模板
     */
    private String processWithFreeMarker(String templateName, Object data, GeneratorConfig config)
            throws Exception {

        Map<String, Object> context = createContext(data, config);
        StringWriter writer = new StringWriter();

        // 根据技术栈选择模板路径
        String templatePath = buildTemplatePath(templateName, config);

        try {
            freemarker.template.Template template = freeMarkerConfig.getTemplate(templatePath);
            template.process(context, writer);
        } catch (Exception e) {
            // 尝试默认模板路径
            templatePath = templateName + ".ftl";
            freemarker.template.Template template = freeMarkerConfig.getTemplate(templatePath);
            template.process(context, writer);
        }

        return writer.toString();
    }

    /**
     * 使用FreeMarker处理前端模板
     */
    private String processWithFrontendTemplate(String templateName, Object data, GeneratorConfig config)
            throws Exception {

        Map<String, Object> context = createContext(data, config);
        StringWriter writer = new StringWriter();

        // 前端模板路径
        String templatePath = buildFrontendTemplatePath(templateName, config);

        try {
            Template template = frontendFreeMarkerConfig.getTemplate(templatePath);
            template.process(context, writer);
        } catch (Exception e) {
            log.warn("前端模板未找到: {}, 使用默认模板", templatePath);
            // 回退到后端模板
            return processWithFreeMarker(templateName, data, config);
        }

        return writer.toString();
    }

    /**
     * 使用Velocity处理模板
     */
    private String processWithVelocity(String templateName, Object data, GeneratorConfig config)
            throws Exception {

        VelocityContext context = createVelocityContext(data, config);
        StringWriter writer = new StringWriter();

        // Velocity模板路径
        String templatePath = "templates/" + buildTemplatePath(templateName, config).replace(".ftl", ".vm");

        try {
            velocityEngine.mergeTemplate(templatePath, "UTF-8", context, writer);
        } catch (Exception e) {
            log.warn("Velocity模板未找到: {}, 使用FreeMarker回退", templatePath);
            return processWithFreeMarker(templateName, data, config);
        }

        return writer.toString();
    }

    /**
     * 构建后端模板路径
     */
    private String buildTemplatePath(String templateName, GeneratorConfig config) {
        String ormFramework = config.getOrmFramework().toLowerCase();

        // 根据ORM框架选择模板目录
        switch (ormFramework) {
            case "mybatis-plus":
                return "mybatis-plus/" + templateName + ".ftl";
            case "jpa":
                return "jpa/" + templateName + ".ftl";
            case "jdbc":
                return "jdbc/" + templateName + ".ftl";
            default:
                return templateName + ".ftl";
        }
    }

    /**
     * 构建前端模板路径
     */
    private String buildFrontendTemplatePath(String templateName, GeneratorConfig config) {
        String frontendFramework = config.getFrontendFramework().toLowerCase();

        switch (frontendFramework) {
            case "vue3":
                return "vue3/" + templateName + ".ftl";
            case "react":
                return "react/" + templateName + ".ftl";
            case "angular":
                return "angular/" + templateName + ".ftl";
            default:
                return templateName + ".ftl";
        }
    }

    /**
     * 创建FreeMarker上下文
     */
    private Map<String, Object> createContext(Object data, GeneratorConfig config) {
        Map<String, Object> context = new HashMap<>();

        // 添加数据对象
        if (data instanceof TableMeta) {
            context.put("table", data);
        }
        context.put("config", config);

        // 添加工具类
        context.put("utils", new TemplateUtils());

        // 添加时间戳
        context.put("timestamp", System.currentTimeMillis());
        context.put("date", new java.util.Date());

        return context;
    }

    /**
     * 创建Velocity上下文
     */
    private VelocityContext createVelocityContext(Object data, GeneratorConfig config) {
        VelocityContext context = new VelocityContext();

        if (data instanceof TableMeta) {
            context.put("table", data);
        }
        context.put("config", config);
        context.put("utils", new TemplateUtils());
        context.put("date", new java.util.Date());

        return context;
    }

}
