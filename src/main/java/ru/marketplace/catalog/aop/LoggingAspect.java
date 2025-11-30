package ru.marketplace.catalog.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Аспект для замера времени выполнения методов.
 * Перехватывает методы, помеченные аннотацией {@link ru.marketplace.catalog.aop.annotations.Loggable}.
 */
@Aspect
@Component
public class LoggingAspect {

    /**
     * Обертывает выполнение метода для замера времени.
     *
     * @param joinPoint точка соединения.
     * @return результат выполнения исходного метода.
     * @throws Throwable если метод выбросит исключение.
     */
    @Around("@annotation(ru.marketplace.catalog.aop.annotations.Loggable)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object proceed = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - start;

        System.out.println("[TIMING] " + joinPoint.getSignature() + " executed in " + executionTime + "ms");
        return proceed;
    }
}