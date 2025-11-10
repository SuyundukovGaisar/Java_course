import ru.marketplace.catalog.controller.MarketplaceController;
import ru.marketplace.catalog.repository.InMemoryProductRepository;
import ru.marketplace.catalog.repository.ProductRepository;
import ru.marketplace.catalog.service.*;
import ru.marketplace.catalog.ui.ConsoleView;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        ConsoleView view = new ConsoleView(scanner);

        ProductRepository productRepository = new InMemoryProductRepository();

        ProductService productService = new ProductServiceImpl(productRepository);
        UserService userService = new UserServiceImpl();
        AuditService auditService = new AuditService();

        MarketplaceController controller = new MarketplaceController(view, userService, productService, auditService);

        controller.run();

        scanner.close();
    }
}