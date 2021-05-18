package com.nathan.ex.aop.annotation;

import java.lang.annotation.*;

/**
 * @author nathan.yang
 * @date 2019/11/28
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogInvokedMethod {

}
