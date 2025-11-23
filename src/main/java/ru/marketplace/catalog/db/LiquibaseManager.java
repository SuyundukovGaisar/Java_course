package ru.marketplace.catalog.db;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import ru.marketplace.catalog.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Управляет выполнением миграций базы данных с помощью Liquibase.
 * Отвечает за создание схем и накат изменений (changelogs).
 */
public class LiquibaseManager {

    private final DatabaseConfig config;

    public LiquibaseManager(DatabaseConfig config) {
        this.config = config;
    }

    /**
     * Предварительно создает необходимые схемы, если они не существуют.
     * Имена схем берутся из конфигурационного файла.
     *
     * @param connection активное соединение с БД.
     * @throws SQLException если произошла ошибка SQL.
     */
    private void ensureSchemasExist(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            // Используем config вместо хардкода
            statement.execute("CREATE SCHEMA IF NOT EXISTS " + config.liquibaseSchema() + ";");
            statement.execute("CREATE SCHEMA IF NOT EXISTS " + config.dataSchema() + ";");
            System.out.printf("Схемы '%s' и '%s' проверены/созданы.\n", config.liquibaseSchema(),
                    config.dataSchema());
        }
    }

    /**
     * Запускает миграции Liquibase, используя параметры из конфигурационного файла.
     */
    public void runMigrations() {
        try (Connection connection = DriverManager.getConnection(config.url(), config.user(),
                config.password())) {

            ensureSchemasExist(connection);

            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation
                    (new JdbcConnection(connection));

            database.setLiquibaseSchemaName(config.liquibaseSchema());
            database.setDefaultSchemaName(config.dataSchema());

            Liquibase liquibase = new Liquibase(config.liquibaseChangelogPath(),
                    new ClassLoaderResourceAccessor(), database);

            liquibase.update();
            System.out.println("Миграции Liquibase успешно выполнены.");

        } catch (SQLException | LiquibaseException e) {
            System.err.println("Ошибка при выполнении миграций Liquibase: " + e.getMessage());
            e.printStackTrace();
        }
    }
}