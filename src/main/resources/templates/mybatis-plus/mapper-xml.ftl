<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${config.packageName}.mapper.${table.className}Mapper">

    <resultMap id="BaseResultMap" type="${config.packageName}.entity.${table.className}">
        <#list table.columns as column>
            <#if column.primaryKey>
                <id column="${column.columnName}" property="${column.propertyName}" jdbcType="${column.jdbcType}"/>
            <#else>
                <result column="${column.columnName}" property="${column.propertyName}" jdbcType="${column.jdbcType}"/>
            </#if>
        </#list>
    </resultMap>

    <sql id="Base_Column_List">
        <#list table.columns as column>${column.columnName}<#sep>, </#sep></#list>
    </sql>

    <#if table.hasPrimaryKey>
    <!-- 根据主键查询 -->
    <select id="selectByPrimaryKey" parameterType="${table.primaryKey.javaType}" resultMap="BaseResultMap">
        SELECT
        <include refid="Base_Column_List"/>
        FROM ${table.tableName}
        WHERE ${table.primaryKey.columnName} = <#noparse>#{</#noparse>${table.primaryKey.propertyName}<#noparse>}</#noparse>
        AND deleted = 0
    </select>
    </#if>

    <!-- 分页查询 -->
    <select id="selectPage" parameterType="${config.packageName}.entity.${table.className}" resultMap="BaseResultMap">
        SELECT
        <include refid="Base_Column_List"/>
        FROM ${table.tableName}
        WHERE deleted = 0
        <if test="entity != null">
            <#list table.columns as column>
                <if test="entity.${column.propertyName} != null">
                    AND ${column.columnName} = <#noparse>#{</#noparse>entity.${column.propertyName}<#noparse>}</#noparse>
                </if>
            </#list>
        </if>
        ORDER BY create_time DESC
    </select>

    <#if table.hasPrimaryKey>
    <!-- 逻辑删除 -->
    <update id="logicDelete" parameterType="${table.primaryKey.javaType}">
        UPDATE ${table.tableName}
        SET deleted = 1, update_time = NOW()
        WHERE ${table.primaryKey.columnName} = <#noparse>#{</#noparse>${table.primaryKey.propertyName}<#noparse>}</#noparse>
        AND deleted = 0
    </update>
    </#if>

    <!-- 批量插入 -->
    <insert id="batchInsert" parameterType="java.util.List">
        INSERT INTO ${table.tableName} (
        <#list table.columns as column>
            <#if !column.isAutoIncrement>${column.columnName}<#sep>, </#sep></#if>
        </#list>
        ) VALUES
        <foreach collection="list" item="item" separator=",">
            (
            <#list table.columns as column>
                <#if !column.isAutoIncrement><#noparse>#{</#noparse>item.${column.propertyName}<#noparse>}</#noparse><#sep>, </#sep></#if>
            </#list>
            )
        </foreach>
    </insert>
</mapper>