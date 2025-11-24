package ru.marketplace.catalog.repository.impl;

import ru.marketplace.catalog.db.ConnectionFactory;
import ru.marketplace.catalog.exception.RepositoryException;
import ru.marketplace.catalog.model.AuditLog;
import ru.marketplace.catalog.repository.AuditRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcAuditRepository implements AuditRepository {

    private final ConnectionFactory connectionFactory;

    private static final String SAVE_SQL = "INSERT INTO marketplace.audit_logs (event_date, username, action)" +
            " VALUES (?, ?, ?)";
    private static final String FIND_ALL_SQL = "SELECT id, event_date, username, action FROM " +
            "marketplace.audit_logs ORDER BY event_date DESC";

    public JdbcAuditRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public void save(AuditLog log) throws RepositoryException {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(SAVE_SQL)) {

            statement.setTimestamp(1, Timestamp.valueOf(log.getTimestamp()));
            statement.setString(2, log.getUsername());
            statement.setString(3, log.getAction());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RepositoryException("Ошибка сохранения лога аудита", e);
        }
    }

    @Override
    public List<AuditLog> findAll() throws RepositoryException {
        List<AuditLog> logs = new ArrayList<>();
        try (Connection connection = connectionFactory.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(FIND_ALL_SQL)) {

            while (rs.next()) {
                logs.add(new AuditLog(
                        rs.getLong("id"),
                        rs.getTimestamp("event_date").toLocalDateTime(),
                        rs.getString("username"),
                        rs.getString("action")
                ));
            }
        } catch (SQLException e) {
            throw new RepositoryException("Ошибка чтения логов аудита", e);
        }
        return logs;
    }
}