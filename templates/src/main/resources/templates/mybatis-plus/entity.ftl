package ${config.packageName}.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
<#if config.generateSwagger>
    import io.swagger.annotations.ApiModel;
    import io.swagger.annotations.ApiModelProperty;
</#if>
import java.io.Serializable;
import java.time.LocalDateTime;

/**
* ${table.tableComment!table.tableName} 实体类
*
* @author ${config.author}
* @version ${config.version}
* @date ${.now?string("yyyy-MM-dd")}
*/
@Data
<#if config.generateLombok>
    @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
</#if>
<#if config.generateSwagger>
    @ApiModel("${table.tableComment!table.tableName}")
</#if>
@TableName("${table.tableName}")
public class ${table.className} implements Serializable {

private static final long serialVersionUID = 1L;

<#list table.columns as column>
    <#if config.generateSwagger>
        @ApiModelProperty("${column.columnComment!column.columnName}")
    </#if>
    <#if column.primaryKey>
        @TableId(value = "${column.columnName}", type = IdType.AUTO)
    <#else>
        @TableField("${column.columnName}")
    </#if>
    <#if column.propertyName == "deleted">
        @TableLogic
    </#if>
    <#if column.propertyName == "createTime" || column.propertyName == "updateTime">
        @TableField(fill = FieldFill.INSERT_UPDATE)
    </#if>
    private ${column.javaType} ${column.propertyName};

</#list>

<#if !table.columns?seq_contains("createTime")>
    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

</#if>
<#if !table.columns?seq_contains("updateTime")>
    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

</#if>
<#if !table.columns?seq_contains("deleted")>
    /** 逻辑删除 */
    @TableLogic
    private Integer deleted;

</#if>
}