package ${config.packageName}.entity;

import javax.persistence.*;
<#if config.generateLombok>
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(callSuper = false)
</#if>
<#if config.generateSwagger>
@ApiModel("${table.tableComment!table.tableName}")
</#if>
@Entity
@Table(name = "${table.tableName}")
public class ${table.className} implements Serializable {

    private static final long serialVersionUID = 1L;

<#list table.columns as column>
    <#if config.generateSwagger>
        @ApiModelProperty("${column.columnComment!column.columnName}")
    </#if>
    <#if table.hasPrimaryKey && column.isPrimaryKey>
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
    <#else>
        @Basic
    </#if>
    @Column(name = "${column.columnName}"<#if !column.nullable>, nullable = false</#if>)
    private ${column.javaType} ${column.propertyName};
</#list>

<#if config.generateAuditing>
    /** 创建时间 */
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    /** 更新时间 */
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /** 创建人 */
    @Column(name = "create_by", updatable = false)
    private String createBy;

    /** 更新人 */
    @Column(name = "update_by")
    private String updateBy;
</#if>

<#if config.generateLogicDelete>
    /** 逻辑删除标志 */
    @Column(name = "deleted")
    private Boolean deleted = false;
</#if>

<#if !config.generateLombok>
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