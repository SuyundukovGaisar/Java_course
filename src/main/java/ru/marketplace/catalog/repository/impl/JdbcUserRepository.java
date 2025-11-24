package ru.marketplace.catalog.repository.impl;

import ru.marketplace.catalog.db.ConnectionFactory;
import ru.marketplace.catalog.exception.RepositoryException;
import ru.marketplace.catalog.model.User;
import ru.marketplace.catalog.repository.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Реализация репозитория пользователей с использованием JDBC.
 * Выполняет SQL-запросы к PostgreSQL.
 */
public class JdbcUserRepository implements UserRepository {

    private final ConnectionFactory connectionFactory;

    private static final String SAVE_SQL = "INSERT INTO marketplace.users (login, password) VALUES (?, ?)";
    private static final String FIND_BY_LOGIN_SQL = "SELECT id, login, password FROM marketplace.users WHERE login = ?";
    private static final String EXISTS_BY_LOGIN_SQL = "SELECT 1 FROM marketplace.users WHERE login = ?";

    public JdbcUserRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(User user) throws RepositoryException {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(SAVE_SQL)) {

            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RepositoryException("Ошибка при сохранении пользователя в БД", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> findByLogin(String login) throws RepositoryException {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_LOGIN_SQL)) {

            statement.setString(1, login);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new User(
                            rs.getString("login"),
                            rs.getString("password")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Ошибка при поиске пользователя по логину", e);
        }
        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsByLogin(String login) throws RepositoryException {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(EXISTS_BY_LOGIN_SQL)) {

            statement.setString(1, login);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RepositoryException("Ошибка при проверке существования пользователя", e);
        }
    }
}