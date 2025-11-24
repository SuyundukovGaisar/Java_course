package ru.marketplace.catalog.service.impl;

import ru.marketplace.catalog.exception.RepositoryException;
import ru.marketplace.catalog.model.AuditLog;
import ru.marketplace.catalog.repository.AuditRepository;
import ru.marketplace.catalog.service.AuditService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервиса аудита.
 * Сохраняет события в репозиторий и читает их оттуда.
 * Не хранит состояние в памяти.
 */
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;

    public AuditServiceImpl(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    /**
     * Формирует и сохраняет запись о действии пользователя.
     * Создает объект AuditLog и передает его в репозиторий.
     *
     * @param username имя пользователя.
     * @param action описание действия.
     */
    @Override
    public void logAction(String username, String action) {
        try {
            AuditLog log = new AuditLog(username, action);
            auditRepository.save(log);
        } catch (RepositoryException e) {
            System.err.println("Не удалось записать аудит: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Возвращает все записи аудита в строковом представлении.
     *
     * @return список строк с логами.
     */
    @Override
    public List<String> getAuditLog() {
        try {
            return auditRepository.findAll().stream()
                    .map(AuditLog::toString)
                    .collect(Collectors.toList());
        } catch (RepositoryException e) {
            System.err.println("Не удалось прочитать аудит: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}