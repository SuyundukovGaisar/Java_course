package ru.marketplace.catalog.repository.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.marketplace.catalog.model.Product;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.marketplace.starter.service.AuditService;

import java.util.List;

/**
 * Интеграционный тест репозитория с использованием Testcontainers и Spring Boot Test.
 * Проверяет работу с реальной БД PostgreSQL.
 */
@JdbcTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JdbcProductRepository.class)
class JdbcProductRepositoryTest {

    /**
     * Контейнер с PostgreSQL.
     */
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("test_db")
            .withUsername("test_user")
            .withPassword("test_pass")
            .withInitScript("init-schemas.sql");

    @DynamicPropertySource
    static void configureProperties(org.springframework.test.context.DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private JdbcProductRepository repository;

    @MockBean
    private AuditService auditService;

    @Test
    void testSaveAndFindAll() {
        Product product = new Product("Electronics", "Samsung Test", 50000);

        repository.save(product);

        List<Product> allProducts = repository.findAll();
        Assertions.assertFalse(allProducts.isEmpty(), "Список продуктов не должен быть пустым");

        boolean found = allProducts.stream()
                .anyMatch(p -> p.getBrand().equals("Samsung Test"));

        Assertions.assertTrue(found, "Сохраненный продукт должен быть найден");
    }
}