package com.small.rose.core.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Project: code-generator
 * @Author: 张小菜
 * @Description: [ ColumnMeta ] 说明： 无
 * @Function: 功能描述： 无
 * @Date: 2025/11/16 016 22:58
 * @Version: v1.0
 */


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColumnMeta {

    private String columnName;        // 列名
    private String propertyName;      // 属性名
    private String dataType;          // 数据库类型
    private String javaType;          // Java类型
    private String jdbcType;          // JDBC类型
    private String columnComment;     // 列注释
    private Boolean isPrimaryKey = false;     // 是否主键
    private Boolean isAutoIncrement = false; // 是否自增
    private Integer length;           // 长度
    private Integer scale;            // 小数位
    private Boolean nullable = true;  // 是否可空
    private String defaultValue;      // 默认值

    public boolean isPrimaryKey(){
        return this.isPrimaryKey ;
    }
}
