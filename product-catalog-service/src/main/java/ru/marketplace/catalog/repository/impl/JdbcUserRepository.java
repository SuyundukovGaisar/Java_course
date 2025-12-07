package ru.marketplace.catalog.repository.impl;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.marketplace.catalog.exception.MarketplaceDataException;
import ru.marketplace.catalog.model.User;
import ru.marketplace.catalog.repository.UserRepository;

import java.util.List;
import java.util.Optional;

/**
 * Реализация репозитория пользователей с использованием Spring JDBC.
 */
@Repository
public class JdbcUserRepository implements UserRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcUserRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<User> USER_ROW_MAPPER = (rs, rowNum) -> new User(
            rs.getString("login"),
            rs.getString("password")
    );

    @Override
    public void save(User user) {
        try {
            String sql = "INSERT INTO marketplace.users (login, password) VALUES (:login, :password)";
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("login", user.getLogin())
                    .addValue("password", user.getPassword());
            jdbcTemplate.update(sql, params);
        } catch (DataAccessException e) {
            throw new MarketplaceDataException("Ошибка при сохранении пользователя: " + user.getLogin(), e);
        }
    }

    @Override
    public Optional<User> findByLogin(String login) {
        try {
            String sql = "SELECT login, password FROM marketplace.users WHERE login = :login";
            List<User> users = jdbcTemplate.query(sql, new MapSqlParameterSource("login", login),
                    USER_ROW_MAPPER);
            return users.stream().findFirst();
        } catch (DataAccessException e) {
            throw new MarketplaceDataException("Ошибка при поиске пользователя: " + login, e);
        }
    }

    @Override
    public boolean existsByLogin(String login) {
        try {
            String sql = "SELECT count(*) FROM marketplace.users WHERE login = :login";
            Integer count = jdbcTemplate.queryForObject(sql, new MapSqlParameterSource("login",
                    login), Integer.class);
            return count != null && count > 0;
        } catch (DataAccessException e) {
            throw new MarketplaceDataException("Ошибка проверки пользователя: " + login, e);
        }
    }
}