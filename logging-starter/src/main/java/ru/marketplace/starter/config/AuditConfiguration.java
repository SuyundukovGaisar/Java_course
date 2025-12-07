package ru.marketplace.starter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.marketplace.starter.aop.AuditAspect;
import ru.marketplace.starter.service.AuditService;

/**
 * Конфигурация для создания аспекта аудита.
 * Загружается только при наличии аннотации {@link ru.marketplace.starter.annotation.EnableAudit}.
 */
@Configuration
public class AuditConfiguration {

    @Bean
    public AuditAspect auditAspect(AuditService auditService) {
        return new AuditAspect(auditService);
    }
}