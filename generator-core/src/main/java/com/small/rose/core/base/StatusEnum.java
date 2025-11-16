package com.small.rose.core.base;

/**
 * @Project: code-generator
 * @Author: 张小菜
 * @Description: [ StatusEnum ] 枚举类
 * @Function: 枚举功能描述： 无
 * @Date: 2025/11/16 016 23:29
 * @Version: v1.0
 */
public enum StatusEnum {
    SUCCESS(200, "请求处理成功"),
    UNAUTHORIZED(401, "用户认证失败"),
    FORBIDDEN(403, "权限不足"),
    SERVICE_ERROR(500, "服务器去旅行了，请稍后重试"),
    PARAM_INVALID(1000, "无效的参数"),
    ;

    public final Integer code;

    public final String message;

    StatusEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }


}
