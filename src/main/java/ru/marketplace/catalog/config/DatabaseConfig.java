package ru.marketplace.catalog.config;

import org.aeonbits.owner.Config;

/**
 * Интерфейс для доступа к конфигурации приложения.
 * Использует библиотеку OWNER для чтения свойств из файла.
 */
@Config.Sources("classpath:application.properties")
public interface DatabaseConfig extends Config {

    /**
     * Возвращает URL для подключения к базе данных.
     * @return URL базы данных.
     */
    @Key("db.url")
    String url();

    /**
     * Возвращает имя пользователя для подключения к базе данных.
     * @return имя пользователя.
     */
    @Key("db.user")
    String user();

    /**
     * Возвращает пароль для подключения к базе данных.
     * @return пароль.
     */
    @Key("db.password")
    String password();

    /**
     * Возвращает путь к главному файлу миграций Liquibase.
     * @return путь к файлу миграций.
     */
    @Key("liquibase.changelog.path")
    String liquibaseChangelogPath();
}