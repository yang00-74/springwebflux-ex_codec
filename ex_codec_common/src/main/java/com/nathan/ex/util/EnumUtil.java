package com.nathan.ex.util;

import com.nathan.ex.enumerate.BaseEnum;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Objects;

@Slf4j
public class EnumUtil {

    public static <E extends BaseEnum, T extends Number> E getEnum(T value, final Class<E> enumClass) {
        E[] es = enumClass.getEnumConstants();
        E undefined = null;
        for (E e : es) {
            if (Objects.equals(e.getCode(), value)) {
                return e;
            } else if (Objects.equals(e.getCode(), -1)) {
                undefined = e;
            }
        }
        return undefined;
    }

    public static <E extends Enum<E>> E getEnumByCode(Object value, final Class<E> enumClass) {
        try {
            return valueOf(enumClass, value, enumClass.getMethod("getCode"));
        } catch (NoSuchMethodException e) {
            log.error("Error: NoSuchMethod in {}.  Cause:{}", e, enumClass.getName());
        }
        return null;
    }

    private static <E extends Enum<?>> E valueOf(Class<E> enumClass, Object value, Method method) {
        E[] es = enumClass.getEnumConstants();
        for (E e : es) {
            Object enumValue;
            try {
                method.setAccessible(true);
                enumValue = method.invoke(e);
            } catch (IllegalAccessException | InvocationTargetException e1) {
                log.error("Error: NoSuchMethod in {}.  Cause:{}", e, enumClass.getName());
                return null;
            }
            if (value instanceof Number && enumValue instanceof Number
                    && new BigDecimal(String.valueOf(value)).compareTo(new BigDecimal(String.valueOf(enumValue))) == 0) {
                return e;
            }
            if (Objects.equals(enumValue, value)) {
                return e;
            }
        }
        return null;
    }
}
