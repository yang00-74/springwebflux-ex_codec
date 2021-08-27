package com.nathan.ex.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ApiResult<T> implements Serializable {

    private Integer ret;

    private String msg;

    private T data;

    private ApiResult(int code, String msg, T data) {
        this.ret = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ApiResult<T> instance(int code, String msg, T t) {
        return new ApiResult<>(code, msg, t);
    }

    public static ApiResult success() {
        return ApiResult.instance(200, "请求成功", null);
    }

    public static <T> ApiResult<T> success(T t) {
        return ApiResult.instance(200, "请求成功", t);
    }

    public static <T> ApiResult<T> success(String msg, T t) {
        return ApiResult.instance(200, msg, t);
    }

    public static <T> ApiResult<T> fail() {
        return ApiResult.instance(500, "请求失败", null);
    }

    public static <T> ApiResult<T> fail(String msg) {
        return ApiResult.instance(500, msg, null);
    }

    public static ApiResult fail(int code, String msg) {
        return ApiResult.instance(code, msg, null);
    }

}
