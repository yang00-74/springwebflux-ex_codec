package com.nathan.ex.util;

import com.nathan.ex.constant.PrefixConstant;

import java.util.UUID;

/**
 * @author nathan.yang
 * @date 2019/11/28
 */
public class TokenUtil {

    public static String getMethodToken() {
        return PrefixConstant.METHOD_TOKEN_PRE + UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String getTraceId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
