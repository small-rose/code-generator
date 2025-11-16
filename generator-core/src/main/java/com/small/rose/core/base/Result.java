package com.small.rose.core.base;

import lombok.Data;

import java.io.Serializable;

/**
 * @Project: code-generator
 * @Author: 张小菜
 * @Description: [ Result ] 说明： 无
 * @Function: 功能描述： 无
 * @Date: 2025/11/16 016 23:03
 * @Version: v1.0
 */

@Data
public class Result<T> implements Serializable {

    private boolean success;
    private String message;
    private T data;
    private Integer code;

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setSuccess(true);
        result.setCode(200);
        result.setMessage("成功");
        result.setData(data);
        return result;
    }

    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.setSuccess(false);
        result.setCode(500);
        result.setMessage(message);
        return result;
    }

    public static <T> Result<T> error(StatusEnum statusEnum) {
        Result<T> result = new Result<>();
        result.setSuccess(false);
        result.setCode(statusEnum.code);
        result.setMessage(statusEnum.message);
        return result;
    }
}
