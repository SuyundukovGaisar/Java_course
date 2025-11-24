package ru.marketplace.catalog.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import ru.marketplace.catalog.aop.annotations.Auditable;
import ru.marketplace.catalog.service.AuditService;

@Aspect
public class AuditAspect {

    private static AuditService auditServiceInstance;

    public static void setAuditService(AuditService service) {
        auditServiceInstance = service;
    }

    /**
     * Выполняется ПОСЛЕ успешного завершения метода, помеченного @Auditable.
     */
    @AfterReturning("@annotation(ru.marketplace.catalog.aop.annotations.Auditable)")
    public void logAuditActivity(JoinPoint joinPoint) {
        if (auditServiceInstance == null) {
            System.err.println("AuditService is not initialized in Aspect!");
            return;
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Auditable annotation = signature.getMethod().getAnnotation(Auditable.class);
        String action = annotation.action();

        String username = "anonymous_user";

        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof String) {
            username = (String) args[0];
        }

        auditServiceInstance.logAction(username, action);
        System.out.println("[AUDIT ASPECT] Action '" + action + "' logged for user '" + username + "'");
    }
}