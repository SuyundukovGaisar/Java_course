package ru.marketplace.starter.annotation;

import org.springframework.context.annotation.Import;
import ru.marketplace.starter.config.AuditConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для включения функциональности аудита в приложении.
 * При её использовании будет создан бин {@link ru.marketplace.starter.aop.AuditAspect}.
 * Приложение должно предоставить реализацию интерфейса {@link ru.marketplace.starter.service.AuditService}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(AuditConfiguration.class)
public @interface EnableAudit {
}