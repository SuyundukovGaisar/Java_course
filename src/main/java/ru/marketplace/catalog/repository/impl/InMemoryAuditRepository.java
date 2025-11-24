package ru.marketplace.catalog.repository.impl;

import ru.marketplace.catalog.model.AuditLog;
import ru.marketplace.catalog.repository.AuditRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Реализация хранилища аудита в оперативной памяти.
 * Используется для тестов или режима работы без базы данных.
 */
public class InMemoryAuditRepository implements AuditRepository {

    private final List<AuditLog> auditLog = new ArrayList<>();

    /**
     * Сохраняет запись аудита в список в памяти.
     *
     * @param record объект записи аудита.
     */
    @Override
    public void save(AuditLog record) {
        auditLog.add(record);
    }

    /**
     * Возвращает все записи аудита из памяти.
     *
     * @return неизменяемый список записей.
     */
    @Override
    public List<AuditLog> findAll() {
        return Collections.unmodifiableList(auditLog);
    }
}