package com.template.support.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * API 요청 및 응답 로깅을 위한 AOP 설정
 */
@Slf4j
@Aspect
@Component
public class LoggingAspect {

    /**
     * presentation 패키지 하위의 모든 컨트롤러 메서드에 대해 로깅 수행
     */
    @Around("execution(* com.template.presentation.api..*Controller.*(..))")
    public Object logApi(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();

        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        log.info("[API Request] {} {} | Method: {}.{} | Args: {}",
                request.getMethod(), request.getRequestURI(), className, methodName, Arrays.toString(args));

        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - start;
            log.info("[API Response] {} {} | Time: {}ms", request.getMethod(), request.getRequestURI(), executionTime);
            return result;
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - start;
            log.error("[API Error] {} {} | Time: {}ms | Message: {}",
                    request.getMethod(), request.getRequestURI(), executionTime, e.getMessage());
            throw e;
        }
    }
}
