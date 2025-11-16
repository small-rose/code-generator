package com.small.rose.core.bean;

import lombok.Data;

/**
 * @Project: code-generator
 * @Author: 张小菜
 * @Description: [ GeneratorRequest ] 说明： 无
 * @Function: 功能描述： 无
 * @Date: 2025/11/17 017 0:13
 * @Version: v1.0
 */

@Data
public class GeneratorRequest {
    private GeneratorConfig config;
    private String tableName;
    private Boolean preview = false;

}
