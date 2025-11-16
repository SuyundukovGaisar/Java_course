package ru.marketplace.catalog;

import org.aeonbits.owner.ConfigFactory;
import ru.marketplace.catalog.config.DatabaseConfig;
import ru.marketplace.catalog.controller.MarketplaceController;
import ru.marketplace.catalog.db.ConnectionFactory;
import ru.marketplace.catalog.db.LiquibaseManager;
import ru.marketplace.catalog.repository.JdbcProductRepository;
import ru.marketplace.catalog.repository.JdbcUserRepository;
import ru.marketplace.catalog.repository.ProductRepository;
import ru.marketplace.catalog.repository.UserRepository;
import ru.marketplace.catalog.service.*;
import ru.marketplace.catalog.ui.ConsoleView;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DatabaseConfig config = ConfigFactory.create(DatabaseConfig.class);

        LiquibaseManager liquibaseManager = new LiquibaseManager(config);
        liquibaseManager.runMigrations();

        Scanner scanner = new Scanner(System.in);
        ConsoleView view = new ConsoleView(scanner);

        ConnectionFactory connectionFactory = new ConnectionFactory(config);

        ProductRepository productRepository = new JdbcProductRepository(connectionFactory);
        UserRepository userRepository = new JdbcUserRepository(connectionFactory);

        ProductService productService = new ProductServiceImpl(productRepository);
        UserService userService = new UserServiceImpl(userRepository);
        AuditService auditService = new AuditService();

        MarketplaceController controller = new MarketplaceController(view, userService, productService, auditService);

        controller.run();

        scanner.close();
    }
}