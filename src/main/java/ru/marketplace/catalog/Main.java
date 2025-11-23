package ru.marketplace.catalog;

import org.aeonbits.owner.ConfigFactory;
import ru.marketplace.catalog.config.DatabaseConfig;
import ru.marketplace.catalog.db.ConnectionFactory;
import ru.marketplace.catalog.db.LiquibaseManager;
import ru.marketplace.catalog.repository.AuditRepository;
import ru.marketplace.catalog.repository.ProductRepository;
import ru.marketplace.catalog.repository.RepositoryFactory;
import ru.marketplace.catalog.repository.UserRepository;
import ru.marketplace.catalog.service.AuditService;
import ru.marketplace.catalog.service.ProductService;
import ru.marketplace.catalog.service.UserService;
import ru.marketplace.catalog.service.impl.AuditServiceImpl;
import ru.marketplace.catalog.service.impl.ProductServiceImpl;
import ru.marketplace.catalog.service.impl.UserServiceImpl;
import ru.marketplace.catalog.ui.ConsoleApplicationRunner;
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

        RepositoryFactory repositoryFactory = new RepositoryFactory(config, connectionFactory);

        ProductRepository productRepository = repositoryFactory.createProductRepository();
        UserRepository userRepository = repositoryFactory.createUserRepository();
        AuditRepository auditRepository = repositoryFactory.createAuditRepository();

        ProductService productService = new ProductServiceImpl(productRepository);
        UserService userService = new UserServiceImpl(userRepository);
        AuditService auditService = new AuditServiceImpl(auditRepository);

        ConsoleApplicationRunner runner = new ConsoleApplicationRunner(view, userService, productService,
                auditService);
        runner.run();

        scanner.close();
    }
}