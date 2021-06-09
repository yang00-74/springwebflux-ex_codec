package com.nathan.ex.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 该机制基于注解模式的 RequestMappingHandlerAdapter，在 webflux 中如果使用函数式开发则无效
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Object exceptionHandler(Exception e) {
        log.error("system exception", e);
        String errMsg = e.getMessage();
        if (!StringUtils.isEmpty(errMsg)) {
            return new Object();
        }
        return new Object();
    }

}
