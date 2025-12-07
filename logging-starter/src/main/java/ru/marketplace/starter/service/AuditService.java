package ru.marketplace.starter.service;

import ru.marketplace.starter.model.AuditLog;
import java.util.List;

/**
 * Интерфейс сервиса аудита.
 * Отвечает за фиксацию действий пользователей.
 */
public interface AuditService {

    /**
     * Логирует действие пользователя.
     *
     * @param username имя пользователя.
     * @param action   описание действия.
     */
    void logAction(String username, String action);

    /**
     * Возвращает полную историю действий.
     *
     * @return список записей аудита.
     */
    List<AuditLog> getAuditLog();
}