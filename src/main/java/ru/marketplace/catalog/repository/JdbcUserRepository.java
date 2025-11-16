package ru.marketplace.catalog.repository;

import ru.marketplace.catalog.db.ConnectionFactory;
import ru.marketplace.catalog.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class JdbcUserRepository implements UserRepository {

    private final ConnectionFactory connectionFactory;

    private static final String SAVE_SQL = "INSERT INTO marketplace.users (login, password) VALUES (?, ?)";
    private static final String FIND_BY_LOGIN_SQL = "SELECT id, login, password FROM marketplace.users WHERE login = ?";
    private static final String EXISTS_BY_LOGIN_SQL = "SELECT 1 FROM marketplace.users WHERE login = ?";

    public JdbcUserRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public void save(User user) {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(SAVE_SQL)) {
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении пользователя", e);
        }
    }

    @Override
    public Optional<User> findByLogin(String login) {
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
            throw new RuntimeException("Ошибка при поиске пользователя по логину", e);
        }
        return Optional.empty();
    }

    @Override
    public boolean existsByLogin(String login) {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(EXISTS_BY_LOGIN_SQL)) {
            statement.setString(1, login);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при проверке существования пользователя", e);
        }
    }
}