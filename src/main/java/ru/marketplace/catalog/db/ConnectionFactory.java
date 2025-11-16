package ru.marketplace.catalog.db;

import ru.marketplace.catalog.config.DatabaseConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Фабрика для создания соединений с базой данных.
 * Использует конфигурацию для получения параметров подключения.
 */
public class ConnectionFactory {

    private final DatabaseConfig config;

    public ConnectionFactory(DatabaseConfig config) {
        this.config = config;
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Не найден JDBC-драйвер для PostgreSQL!", e);
        }
    }

    /**
     * Создает и возвращает новое соединение с базой данных.
     * @return объект Connection.
     * @throws SQLException если не удалось установить соединение.
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(config.url(), config.user(), config.password());
    }
}