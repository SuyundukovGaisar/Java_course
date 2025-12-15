package ru.marketplace.catalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.marketplace.starter.annotation.EnableAudit;

/**
 * Главный класс приложения (Точка входа).
 * <p>
 * Аннотация {@link SpringBootApplication} включает:
 * <ul>
 *     <li>{@code @Configuration}: Класс является источником определений бинов.</li>
 *     <li>{@code @EnableAutoConfiguration}: Spring Boot автоматически настраивает приложение
 *     на основе зависимостей (создает DataSource, настраивает Web MVC и т.д.).</li>
 *     <li>{@code @ComponentScan}: Сканирует пакеты на наличие компонентов (@Service, @Repository, @Controller).</li>
 * </ul>
 */
@SpringBootApplication
@EnableAudit
public class MarketplaceApplication {

    /**
     * Запускает приложение.
     *
     * @param args аргументы командной строки.
     */
    public static void main(String[] args) {
        SpringApplication.run(MarketplaceApplication.class, args);
    }
}