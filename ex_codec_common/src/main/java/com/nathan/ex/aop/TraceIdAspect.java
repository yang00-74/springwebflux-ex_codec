package com.nathan.ex.aop;

import com.nathan.ex.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * @author nathan.yang
 * @date 2020/2/21
 */
@Slf4j
@Aspect
@Component
public class TraceIdAspect {

    private static final String TRACE_ID = "trace_id";

    @Around(value = "@annotation(com.nathan.ex.aop.annotation.TraceId)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        String traceId = TokenUtil.getTraceId();
        MDC.put(TRACE_ID, traceId);
        try {
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            throw throwable;
        } finally {
            MDC.remove(TRACE_ID);
        }
    }
}
