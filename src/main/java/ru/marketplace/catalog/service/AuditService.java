package ru.marketplace.catalog.service;

import java.util.List;

/**
 * Сервис для управления логикой аудита.
 */
public interface AuditService {
    /**
     * Формирует и сохраняет запись о действии пользователя.
     * @param username имя пользователя.
     * @param action описание действия.
     */
    void logAction(String username, String action);

    /**
     * Возвращает все записи аудита.
     * @return список всех записей.
     */
    List<String> getAuditLog();
}