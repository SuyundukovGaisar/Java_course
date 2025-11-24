package ru.marketplace.catalog.repository;

import ru.marketplace.catalog.config.DatabaseConfig;
import ru.marketplace.catalog.db.ConnectionFactory;
import ru.marketplace.catalog.repository.impl.*;

public class RepositoryFactory {
    private final DatabaseConfig config;
    private final ConnectionFactory connectionFactory;

    public RepositoryFactory(DatabaseConfig config, ConnectionFactory connectionFactory) {
        this.config = config;
        this.connectionFactory = connectionFactory;
    }

    public ProductRepository createProductRepository() {
        if ("memory".equalsIgnoreCase(config.repositoryType())) {
            return new InMemoryProductRepository();
        }
        return new JdbcProductRepository(connectionFactory);
    }

    public UserRepository createUserRepository() {
        return new JdbcUserRepository(connectionFactory);
    }

    public AuditRepository createAuditRepository() {
        if ("memory".equalsIgnoreCase(config.repositoryType())) {
            return new InMemoryAuditRepository();
        }
        return new JdbcAuditRepository(connectionFactory);
    }
}