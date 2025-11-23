package ru.marketplace.catalog.repository;

import ru.marketplace.catalog.exception.RepositoryException;
import ru.marketplace.catalog.model.AuditLog;
import java.util.List;

public interface AuditRepository {
    void save(AuditLog log) throws RepositoryException;
    List<AuditLog> findAll() throws RepositoryException;
}