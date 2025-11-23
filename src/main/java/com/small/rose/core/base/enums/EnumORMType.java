package com.small.rose.core.base.enums;

/**
 * @Project: code-generator
 * @Author: 张小菜
 * @Description: [ EnumORMType ] 枚举类
 * @Function: 枚举功能描述： 无
 * @Date: 2025/11/23 023 21:13
 * @Version: v1.0
 */
public enum EnumORMType {

    MYBATIS_PLUS("mybatis-plus", "orm使用mybatis-plus"),
    JPA("jpa", "orm使用jpa"),
    JDBC_TEMPLATE("jdbctemplate", "orm使用jdbctemplate"),
    ;

    private String code ;
    private String desc ;

    public String getCode(){
        return code;
    }

    public String getDesc(){
        return desc;
    }

    EnumORMType(String code, String desc){
        this.code = code;
        this.desc = desc;
    }
}
