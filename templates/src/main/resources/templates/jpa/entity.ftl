package ${config.packageName}.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
<#if config.generateSwagger>
    import io.swagger.annotations.ApiModel;
    import io.swagger.annotations.ApiModelProperty;
</#if>
import javax.persistence.*;
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
@Entity
<#if config.generateSwagger>
    @ApiModel("${table.tableComment!table.tableName}")
</#if>
@Table(name = "${table.tableName}")
public class ${table.className} implements Serializable {

private static final long serialVersionUID = 1L;

<#list table.columns as column>
    <#if config.generateSwagger>
        @ApiModelProperty("${column.columnComment!column.columnName}")
    </#if>
    <#if column.primaryKey>
        @Id
        <#if column.autoIncrement>
            @GeneratedValue(strategy = GenerationType.IDENTITY)
        </#if>
    </#if>
    @Column(name = "${column.columnName}")
    private ${column.javaType} ${column.propertyName};

</#list>
}