package ru.marketplace.catalog;

import org.aeonbits.owner.ConfigFactory;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import ru.marketplace.catalog.aop.AuditAspect;
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
import ru.marketplace.catalog.web.ProductServlet;
import ru.marketplace.catalog.web.UserServlet;

import java.io.File;

/**
 * Точка входа в приложение.
 * Запускает Embedded Tomcat сервер и регистрирует сервлеты.
 */
public class Main {

    private static final int PORT = 8080;

    public static void main(String[] args) throws LifecycleException {
        DatabaseConfig config = ConfigFactory.create(DatabaseConfig.class);

        LiquibaseManager liquibaseManager = new LiquibaseManager(config);
        liquibaseManager.runMigrations();

        ConnectionFactory connectionFactory = new ConnectionFactory(config);
        RepositoryFactory repositoryFactory = new RepositoryFactory(config, connectionFactory);

        ProductRepository productRepository = repositoryFactory.createProductRepository();
        UserRepository userRepository = repositoryFactory.createUserRepository();
        AuditRepository auditRepository = repositoryFactory.createAuditRepository();

        ProductService productService = new ProductServiceImpl(productRepository);
        UserService userService = new UserServiceImpl(userRepository);
        AuditService auditService = new AuditServiceImpl(auditRepository);

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(PORT);
        tomcat.getConnector();

        Context ctx = tomcat.addContext("", new File(".").getAbsolutePath());

        ProductServlet productServlet = new ProductServlet(productService);
        Tomcat.addServlet(ctx, "ProductServlet", productServlet);
        ctx.addServletMappingDecoded("/products/*", "ProductServlet");

        System.out.println("Запуск сервера на порту: " + PORT);

        UserServlet userServlet = new UserServlet(userService);
        Tomcat.addServlet(ctx, "UserServlet", userServlet);
        ctx.addServletMappingDecoded("/users/*", "UserServlet");
        AuditAspect.setAuditService(auditService);


        tomcat.start();
        tomcat.getServer().await();
    }
}