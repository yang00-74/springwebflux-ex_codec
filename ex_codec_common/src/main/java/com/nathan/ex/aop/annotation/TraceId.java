package com.nathan.ex.aop.annotation;

import java.lang.annotation.*;

/**
 * @author nathan.yang
 * @date 2020/2/21
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TraceId {

}