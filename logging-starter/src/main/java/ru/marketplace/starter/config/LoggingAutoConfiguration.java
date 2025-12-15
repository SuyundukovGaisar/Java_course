package ru.marketplace.starter.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import ru.marketplace.starter.aop.AuditAspect;
import ru.marketplace.starter.aop.LoggingAspect;
import ru.marketplace.starter.service.AuditService;

/**
 * Автоконфигурация для модуля логирования и аудита.
 * Подключается автоматически при наличии стартера в зависимостях.
 */
@Configuration
@EnableAspectJAutoProxy
public class LoggingAutoConfiguration {

    @Bean
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }
}