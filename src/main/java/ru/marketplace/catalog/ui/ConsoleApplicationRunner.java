package ru.marketplace.catalog.ui;

import ru.marketplace.catalog.model.Product;
import ru.marketplace.catalog.model.User;
import ru.marketplace.catalog.service.AuditService;
import ru.marketplace.catalog.service.ProductService;
import ru.marketplace.catalog.service.UserService;

import java.util.List;
import java.util.Optional;

/**
 * Главный контроллер приложения (слой Controller).
 * Управляет логикой, жизненным циклом и взаимодействием между сервисами и представлением.
 */
public class ConsoleApplicationRunner {
    private final ConsoleView view;
    private final UserService userService;
    private final ProductService productService;
    private final AuditService auditService;
    private User currentUser = null;

    // В конструкторе тоже принимаем интерфейс AuditService
    public ConsoleApplicationRunner(ConsoleView view, UserService userService, ProductService productService, AuditService auditService) {
        this.view = view;
        this.userService = userService;
        this.productService = productService;
        this.auditService = auditService;
    }

    /**
     * Запускает главный цикл приложения (меню аутентификации).
     */
    public void run() {
        while (true) {
            String menu = """
                    --- Главное меню ---
                    1. Регистрация
                    2. Вход
                    3. Выход из программы
                    Выберите пункт:\s""";
            int choice = view.promptForMenuChoice(menu);

            switch (choice) {
                case 1 -> registerUser();
                case 2 -> loginUser();
                case 3 -> {
                    view.showMessage("До свидания!");
                    return;
                }
                default -> view.showMessage("Неверный выбор, попробуйте снова.");
            }
        }
    }

    private void registerUser() {
        String login = view.promptForString("Введите логин для регистрации: ");
        String password = view.promptForString("Введите пароль: ");

        if (userService.registerUser(login, password)) {
            view.showMessage("Регистрация прошла успешно!");
        } else {
            view.showMessage("Пользователь с таким логином уже существует.");
        }
    }

    private void loginUser() {
        String login = view.promptForString("Введите логин: ");
        String password = view.promptForString("Введите пароль: ");

        Optional<User> userOpt = userService.loginUser(login, password);
        if (userOpt.isPresent()) {
            currentUser = userOpt.get();
            auditService.logAction(currentUser.getLogin(), "Logged in");
            view.showMessage("Вход выполнен успешно. Добро пожаловать, " + currentUser.getLogin() + "!");
            runMarketplaceMenu();
        } else {
            view.showMessage("Неверный логин или пароль.");
        }
    }

    private void runMarketplaceMenu() {
        while (currentUser != null) {
            String menu = """

                    --- Меню Маркетплейса ---
                    1. Посмотреть каталог товаров
                    2. Добавить товар
                    3. Изменить товар
                    4. Удалить товар
                    5. Найти товары (фильтрация)
                    6. Посмотреть лог действий (аудит)
                    7. Выйти из аккаунта
                    Выберите пункт:\s""";
            int choice = view.promptForMenuChoice(menu);

            switch (choice) {
                case 1 -> viewCatalog();
                case 2 -> addProduct();
                case 3 -> updateProduct();
                case 4 -> deleteProduct();
                case 5 -> filterProducts();
                case 6 -> {
                    List<String> log = auditService.getAuditLog();
                    view.displayAuditLog(log);
                }
                case 7 -> {
                    auditService.logAction(currentUser.getLogin(), "Logged out");
                    currentUser = null;
                    view.showMessage("Вы вышли из своего аккаунта.");
                    return;
                }
                default -> view.showMessage("Неверный выбор.");
            }
        }
    }

    private void viewCatalog() {
        List<Product> products = productService.getAllProducts();
        view.displayProducts(products);
    }

    private void addProduct() {
        String category = view.promptForString("Введите категорию: ");
        String brand = view.promptForString("Введите бренд: ");
        int price = view.promptForInt("Введите цену: ");

        Product product = new Product(category, brand, price);
        productService.addProduct(product);
        auditService.logAction(currentUser.getLogin(), "Added product: " + product);
        view.showMessage("Товар успешно добавлен!");
    }

    private void updateProduct() {
        long id = view.promptForLong("Введите ID товара для изменения: ");

        if (productService.findById(id).isEmpty()) {
            view.showMessage("Товар с таким ID не найден.");
            return;
        }

        String category = view.promptForString("Введите новую категорию: ");
        String brand = view.promptForString("Введите новый бренд: ");
        int price = view.promptForInt("Введите новую цену: ");

        if (productService.updateProduct(id, category, brand, price)) {
            auditService.logAction(currentUser.getLogin(), "Updated product with ID: " + id);
            view.showMessage("Товар успешно обновлен.");
        }
    }

    private void deleteProduct() {
        long id = view.promptForLong("Введите ID товара для удаления: ");
        if (productService.deleteProduct(id)) {
            auditService.logAction(currentUser.getLogin(), "Deleted product with ID: " + id);
            view.showMessage("Товар успешно удален.");
        } else {
            view.showMessage("Товар с таким ID не найден.");
        }
    }

    private void filterProducts() {
        String menu = """
                Фильтровать по:
                1. Категории
                2. Бренду
                Выберите тип фильтра:\s""";
        int choice = view.promptForMenuChoice(menu);

        String filterType = "";
        String value = "";

        switch(choice) {
            case 1 -> {
                filterType = "category";
                value = view.promptForString("Введите название категории: ");
            }
            case 2 -> {
                filterType = "brand";
                value = view.promptForString("Введите название бренда: ");
            }
            default -> {
                view.showMessage("Неверный тип фильтра.");
                return;
            }
        }

        long startTime = System.nanoTime();
        List<Product> filteredProducts = productService.filterBy(filterType, value);
        long endTime = System.nanoTime();

        double duration = (endTime - startTime) / 1_000_000.0;
        view.displaySearchResults(filteredProducts, duration);
    }
}