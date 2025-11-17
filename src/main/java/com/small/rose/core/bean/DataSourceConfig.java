package com.small.rose.core.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Project: code-generator
 * @Author: 张小菜
 * @Description: [ DataSourceConfig ] 说明： 无
 * @Function: 功能描述： 无
 * @Date: 2025/11/17 017 0:09
 * @Version: v1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataSourceConfig {

    private String url;
    private String username;
    private String password;
    private String driverClassName;
    private String catalog;
    private String schema;
}
