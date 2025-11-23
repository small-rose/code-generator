package ${config.packageName}.entity;

<#if config.generateLombok>
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import lombok.AllArgsConstructor;
</#if>
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
* @since ${.now?string("yyyy-MM-dd")}
*/
<#if config.generateLombok>
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
</#if>
<#if config.generateSwagger>
    @ApiModel("${table.tableComment!table.tableName}")
</#if>
public class ${table.className} implements Serializable {

    private static final long serialVersionUID = 1L;

<#list table.columns as column>
    <#if config.generateSwagger>
    @ApiModelProperty("${column.columnComment!column.columnName}")
    </#if>
    private ${column.javaType} ${column.propertyName};
</#list>

<#if !config.generateLombok>
<#-- 生成构造方法 -->
    public ${table.className}() {}

    public ${table.className}(<#list table.columns as column>${column.javaType} ${column.propertyName}<#if column_has_next>, </#if></#list>) {
    <#list table.columns as column>
        this.${column.propertyName} = ${column.propertyName};
    </#list>
    }

<#-- 生成getter/setter -->
    <#list table.columns as column>
        public ${column.javaType} get${column.propertyName?cap_first}() {
        return this.${column.propertyName};
        }

        public void set${column.propertyName?cap_first}(${column.javaType} ${column.propertyName}) {
        this.${column.propertyName} = ${column.propertyName};
        }
    </#list>
</#if>

}