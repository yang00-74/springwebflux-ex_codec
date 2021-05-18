package com.nathan.ex.aop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * @author nathan.yang
 * @date 2019/11/28
 */
@Slf4j
@Aspect
@Component
public class LogHandlerAspect {

    @Around(value = "@annotation(com.nathan.ex.aop.annotation.LogInvokedMethod)")
    public Object logAround(ProceedingJoinPoint pjp) {

        InvokedMethodInfo methodInfo = getInvokedMethodInfo(pjp);
        HashMap map = new HashMap(16);

        methodInfo.getArgumentInfoList()
                .forEach(argumentInfo -> map.put(argumentInfo.getName(), getParamJson(argumentInfo.getValue())));

        RequestMapping annotation = methodInfo.getAnnotations().stream()
                .filter(an -> Objects.equals(an.annotationType(), RequestMapping.class))
                .map(an -> (RequestMapping) an)
                .findFirst()
                .orElse(null);
        if (null != annotation) {
            String[] path = annotation.value();
            log.info("path:{}, {}.{}, InputArguments: {}", path[0],methodInfo.getInvokedClass(), methodInfo.getInvokedMethod(),
                    map.toString());
        } else {
            log.info("{}.{}, InputArguments: {}", methodInfo.getInvokedClass(), methodInfo.getInvokedMethod(),
                    map.toString());
        }
        try {
            Object res = pjp.proceed();
            log.info("{}.{}, Return: {}", methodInfo.getInvokedClass(), methodInfo.getInvokedMethod(),
                    getParamJson(res));
            return res;
        } catch (Throwable e) {
            log.error("Error", e);
        }
        return null;
    }

    private Object getParamJson(Object value) {
        if (value == null) {
            return "null";
        } else {
            return value.toString();
        }
    }

    private InvokedMethodInfo getInvokedMethodInfo(ProceedingJoinPoint pjp) {
        List<ArgumentInfo> argumentInfos = new ArrayList<>();
        Object[] args = pjp.getArgs();
        Class<?>[] paramsClass = new Class[args.length];

        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        String[] parameterNames = methodSignature.getParameterNames();
        for (int i = 0; i < args.length; i++) {
            if (args[i] != null) {
                paramsClass[i] = args[i].getClass();
            } else {
                paramsClass[i] = null;
            }
            ArgumentInfo p = new ArgumentInfo(paramsClass[i], parameterNames[i], args[i]);
            argumentInfos.add(p);
        }
        Annotation[] annotations = methodSignature.getMethod().getAnnotations();
        String name = pjp.getThis().getClass().getName();
        String clzName = name.substring(name.lastIndexOf(".") + 1, name.indexOf("$"));
        return new InvokedMethodInfo(clzName, pjp.getSignature().getName(), argumentInfos, Arrays.asList(annotations));
    }

    @Data
    @AllArgsConstructor
    private static class ArgumentInfo {
        private Class clz;
        private String name;
        private Object value;
    }

    @Data
    @AllArgsConstructor
    private static class InvokedMethodInfo {
        private String invokedClass;
        private String invokedMethod;
        private List<ArgumentInfo> argumentInfoList;
        private List<Annotation> annotations;
    }

}
