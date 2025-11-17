package com.small.rose.core.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Project: code-generator
 * @Author: 张小菜
 * @Description: [ TemplateUtils ] 说明：
 *    模板工具类，提供模板中使用的工具方法
 * @Function: 功能描述： 无
 * @Date: 2025/11/16 016 23:15
 * @Version: v1.0
 */
public class TemplateUtils {

    /**
     * 获取当前时间
     */
    public String now() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 首字母大写
     */
    public String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * 首字母小写
     */
    public String uncapitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    /**
     * 下划线转驼峰
     */
    public String toCamelCase(String str, boolean firstCharUpper) {
        if (str == null || str.isEmpty()) return str;

        StringBuilder result = new StringBuilder();
        boolean toUpper = firstCharUpper;

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            if (c == '_') {
                toUpper = true;
            } else {
                if (toUpper) {
                    result.append(Character.toUpperCase(c));
                    toUpper = false;
                } else {
                    result.append(Character.toLowerCase(c));
                }
            }
        }

        return result.toString();
    }

    /**
     * 获取需要导入的包
     */
    public List<String> getImportPackages(List<String> javaTypes) {
        return javaTypes.stream()
                .filter(type -> type.startsWith("java.time.") || type.startsWith("java.math."))
                .map(type -> {
                    if (type.startsWith("java.time.")) {
                        return "java.time.*";
                    } else if (type.startsWith("java.math.")) {
                        return "java.math.*";
                    }
                    return type;
                })
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * 生成字段注释
     */
    public String generateFieldComment(String comment, boolean required) {
        if (comment == null || comment.isEmpty()) {
            return required ? "必填字段" : "";
        }
        return comment + (required ? " (必填)" : "");
    }
}
