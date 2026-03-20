package com.oz.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    // Pointcut: применяем ко всем методам, помеченным @LogExecutionTime
    @Around("@annotation(com.oz.common.config.LogExecutionTime)")
    public Object logTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        try {
            // Выполняем сам метод (твой handleOrderCreated)
            return joinPoint.proceed();
        } finally {
            long executionTime = System.currentTimeMillis() - start;
            // joinPoint.getSignature().getName() подставит имя метода автоматически
            log.info("Метод [{}] выполнен за {} мс",
                    joinPoint.getSignature().getName(), executionTime);
        }
    }
}