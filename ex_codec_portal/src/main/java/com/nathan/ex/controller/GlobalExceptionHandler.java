package com.nathan.ex.controller;

import com.nathan.ex.dto.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.WebExchangeBindException;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 该机制基于注解模式的 RequestMappingHandlerAdapter，在 webflux 中如果使用函数式开发则无效
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ApiResult exceptionHandler(Exception e) {
        log.error("system exception", e);
        String errMsg = e.getMessage();
        if (!StringUtils.isEmpty(errMsg)) {
            return ApiResult.fail(errMsg);
        }
        return ApiResult.fail();
    }

    // 处理 form data方式调用接口校验失败抛出的异常
    // 处理 GET 方法校验失败抛出的异常
    @ResponseBody
    @ExceptionHandler(BindException.class)
    public ApiResult bindExceptionHandler(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        String collect = fieldErrors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(";"));
        return ApiResult.fail(400, collect);
    }

    // 处理方法单个入参校验失败抛出的异常
    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResult constraintViolationException(ConstraintViolationException e) {
        String str = e.getLocalizedMessage();
        if (!StringUtils.isEmpty(str)) {
            String[] array = str.split(":");
            int index = 0;
            if (array.length == 2) {
                index = 1;
            }
            str = array[index];
        }
        return ApiResult.fail(400, str);
    }

    // WebMVC 处理 json 请求体调用接口校验失败抛出的异常
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResult methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        String collect = fieldErrors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(";"));
        return ApiResult.fail(400, collect);
    }

    // WebFlux 处理 json 请求体调用接口校验失败抛出的异常
    @ResponseBody
    @ExceptionHandler(WebExchangeBindException.class)
    public ApiResult WebExchangeBindException(WebExchangeBindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        String collect = fieldErrors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(";"));
        return ApiResult.fail(400, collect);
    }

}
