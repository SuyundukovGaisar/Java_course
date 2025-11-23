package ru.marketplace.catalog.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.marketplace.catalog.config.DatabaseConfig;
import ru.marketplace.catalog.db.ConnectionFactory;
import ru.marketplace.catalog.db.LiquibaseManager;
import ru.marketplace.catalog.exception.RepositoryException;
import ru.marketplace.catalog.model.Product;
import ru.marketplace.catalog.repository.impl.JdbcProductRepository;

import java.util.List;

/**
 * Интеграционный тест для {@link JdbcProductRepository}.
 * Использует библиотеку TestContainers для запуска настоящей базы данных PostgreSQL в Docker.
 * Проверяет, что SQL-запросы в репозитории написаны корректно.
 */
class JdbcProductRepositoryTest {

    /**
     * Контейнер с базой данных. Будет запущен один раз перед всеми тестами.
     * Мы используем тот же образ postgres, что и в docker-compose, или похожий.
     */
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("test_db")
            .withUsername("test_user")
            .withPassword("test_pass");

    static JdbcProductRepository repository;
    static ConnectionFactory connectionFactory;

    /**
     * Метод инициализации. Запускает контейнер, накатывает миграции Liquibase
     * и создает экземпляр репозитория.
     */
    @BeforeAll
    static void startContainer() {
        postgres.start();

        DatabaseConfig testConfig = new DatabaseConfig() {
            @Override public String url() { return postgres.getJdbcUrl(); }
            @Override public String user() { return postgres.getUsername(); }
            @Override public String password() { return postgres.getPassword(); }
            @Override public String liquibaseChangelogPath() { return "db/changelog/db.changelog-master.xml"; }
            @Override public String liquibaseSchema() { return "liquibase_service"; }
            @Override public String dataSchema() { return "marketplace"; }
            @Override public String repositoryType() { return "jdbc"; }
        };

        LiquibaseManager liquibaseManager = new LiquibaseManager(testConfig);
        liquibaseManager.runMigrations();

        connectionFactory = new ConnectionFactory(testConfig);
        repository = new JdbcProductRepository(connectionFactory);
    }

    /**
     * Остановка контейнера после выполнения всех тестов.
     */
    @AfterAll
    static void stopContainer() {
        postgres.stop();
    }

    /**
     * Тест проверяет сценарий:
     * 1. Сохранение нового продукта.
     * 2. Чтение списка продуктов.
     * 3. Проверку, что сохраненный продукт действительно появился в БД с присвоенным ID.
     */
    @Test
    void testSaveAndFindAll() throws RepositoryException {
        Product product = new Product("Electronics", "Samsung Test", 50000);

        repository.save(product);

        List<Product> allProducts = repository.findAll();
        Assertions.assertFalse(allProducts.isEmpty(), "Список продуктов не должен быть пустым");

        Product savedProduct = allProducts.get(allProducts.size() - 1);

        Assertions.assertEquals("Samsung Test", savedProduct.getBrand());
        Assertions.assertNotEquals(0, savedProduct.getId(), "ID должен быть сгенерирован базой данных");
    }
}