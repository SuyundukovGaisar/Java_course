package ru.marketplace.catalog.repository.impl;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.marketplace.catalog.exception.MarketplaceDataException;
import ru.marketplace.catalog.model.AuditLog;
import ru.marketplace.catalog.repository.AuditRepository;

import java.sql.Timestamp;
import java.util.List;

/**
 * Реализация репозитория аудита. Сохраняет события в таблицу audit_logs.
 */
@Repository
public class JdbcAuditRepository implements AuditRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcAuditRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<AuditLog> AUDIT_ROW_MAPPER = (rs, rowNum) -> new AuditLog(
            rs.getLong("id"),
            rs.getTimestamp("event_date").toLocalDateTime(),
            rs.getString("username"),
            rs.getString("action")
    );

    @Override
    public void save(AuditLog log) {
        try {
            String sql = "INSERT INTO marketplace.audit_logs (event_date, username, action) VALUES " +
                    "(:date, :username, :action)";
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("date", Timestamp.valueOf(log.getTimestamp()))
                    .addValue("username", log.getUsername())
                    .addValue("action", log.getAction());

            jdbcTemplate.update(sql, params);
        } catch (DataAccessException e) {
            throw new MarketplaceDataException("Ошибка при записи аудита", e);
        }
    }

    @Override
    public List<AuditLog> findAll() {
        try {
            String sql = "SELECT id, event_date, username, action FROM marketplace.audit_logs " +
                    "ORDER BY event_date DESC";
            return jdbcTemplate.query(sql, AUDIT_ROW_MAPPER);
        } catch (DataAccessException e) {
            throw new MarketplaceDataException("Ошибка при чтении логов аудита", e);
        }
    }
}