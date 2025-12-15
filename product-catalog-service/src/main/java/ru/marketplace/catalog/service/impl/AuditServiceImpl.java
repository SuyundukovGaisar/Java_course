package ru.marketplace.catalog.service.impl;

import org.springframework.stereotype.Service;
import ru.marketplace.starter.model.AuditLog;
import ru.marketplace.catalog.repository.AuditRepository;
import ru.marketplace.starter.service.AuditService;

import java.util.List;

/**
 * Реализация сервиса аудита.
 * Использует {@link AuditRepository} для сохранения данных в БД.
 */
@Service
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;

    public AuditServiceImpl(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @Override
    public void logAction(String username, String action) {
        AuditLog log = new AuditLog(username, action);
        auditRepository.save(log);
    }

    @Override
    public List<AuditLog> getAuditLog() {
        return auditRepository.findAll();
    }
}