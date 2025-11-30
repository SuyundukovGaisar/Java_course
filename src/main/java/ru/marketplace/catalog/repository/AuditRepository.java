package ru.marketplace.catalog.repository;

import ru.marketplace.catalog.model.AuditLog;
import java.util.List;

/**
 * Интерфейс репозитория для работы с аудитом.
 */
public interface AuditRepository {
    void save(AuditLog log);
    List<AuditLog> findAll();
}