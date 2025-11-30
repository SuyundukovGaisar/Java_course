package ru.marketplace.catalog.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import ru.marketplace.catalog.aop.annotations.Auditable;
import ru.marketplace.catalog.service.AuditService;

/**
 * Аспект для аудита действий пользователей.
 * Перехватывает методы, помеченные аннотацией {@link Auditable}.
 * <p>
 * Аннотация {@link Component} делает этот класс бином Spring,
 * что позволяет автоматически внедрять в него {@link AuditService}.
 */
@Aspect
@Component
public class AuditAspect {

    private final AuditService auditService;

    /**
     * Конструктор для внедрения зависимости сервиса аудита.
     * Больше не нужно использовать статические сеттеры.
     *
     * @param auditService сервис для записи логов.
     */
    public AuditAspect(AuditService auditService) {
        this.auditService = auditService;
    }

    /**
     * Выполняется ПОСЛЕ успешного завершения метода, помеченного @Auditable.
     *
     * @param joinPoint точка соединения (перехваченный метод).
     */
    @AfterReturning("@annotation(ru.marketplace.catalog.aop.annotations.Auditable)")
    public void logAuditActivity(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Auditable annotation = signature.getMethod().getAnnotation(Auditable.class);
        String action = annotation.action();

        String username = "anonymous_user";
        Object[] args = joinPoint.getArgs();

        if (args.length > 0 && args[0] instanceof String) {
            username = (String) args[0];
        }

        auditService.logAction(username, action);

        System.out.println("[AUDIT ASPECT] Action '" + action + "' logged for user '" + username + "'");
    }
}