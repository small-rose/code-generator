package com.small.rose.core.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Project: code-generator
 * @Author: 张小菜
 * @Description: [ TableMeta ] 说明： 无
 * @Function: 功能描述： 无
 * @Date: 2025/11/16 016 22:56
 * @Version: v1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableMeta {

    private String tableName;          // 表名
    private String tableComment;      // 表注释
    private String className;         // 类名（大驼峰）
    private String instanceName;     // 实例名（小驼峰）
    private List<ColumnMeta> columns; // 列信息
    private ColumnMeta primaryKey;    // 主键
    private String engine;            // 存储引擎
    private String charset;           // 字符集
}
