package ru.marketplace.catalog.ui;

import ru.marketplace.catalog.model.Product;
import ru.marketplace.catalog.model.User;
import ru.marketplace.catalog.service.AuditService;
import ru.marketplace.catalog.service.ProductService;
import ru.marketplace.catalog.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class UserInterface {
    private final Scanner scanner;
    private final UserService userService = new UserService();
    private final ProductService productService = new ProductService();
    private final AuditService auditService = new AuditService();
    private User currentUser = null;

    public UserInterface(Scanner scanner) {
        this.scanner = scanner;
    }

    public void start() {
        while (true) {
            System.out.println("\n--- Главное меню ---");
            System.out.println("1. Регистрация");
            System.out.println("2. Вход");
            System.out.println("3. Выход из программы");
            System.out.print("Выберите пункт: ");

            int choice = readInt();
            switch (choice) {
                case 1:
                    registerUser();
                    break;
                case 2:
                    loginUser();
                    break;
                case 3:
                    System.out.println("До свидания!");
                    return;
                default:
                    System.out.println("Неверный выбор, попробуйте снова.");
            }
        }
    }

    private void registerUser() {
        System.out.print("Введите логин для регистрации: ");
        String login = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        if (userService.registerUser(login, password)) {
            System.out.println("Регистрация прошла успешно!");
        } else {
            System.out.println("Пользователь с таким логином уже существует.");
        }
    }

    private void loginUser() {
        System.out.print("Введите логин: ");
        String login = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        Optional<User> userOpt = userService.loginUser(login, password);
        if (userOpt.isPresent()) {
            currentUser = userOpt.get();
            auditService.logAction(currentUser.getLogin(), "Logged in");
            System.out.println("Вход выполнен успешно. Добро пожаловать, " + currentUser.getLogin() + "!");
            startMarketplaceMenu();
        } else {
            System.out.println("Неверный логин или пароль.");
        }
    }

    private void startMarketplaceMenu() {
        while (currentUser != null) {
            System.out.println("\n--- Меню Маркетплейса ---");
            System.out.println("1. Посмотреть каталог товаров");
            System.out.println("2. Добавить товар");
            System.out.println("3. Изменить товар");
            System.out.println("4. Удалить товар");
            System.out.println("5. Найти товары (фильтрация)");
            System.out.println("6. Посмотреть лог действий (аудит)");
            System.out.println("7. Выйти из аккаунта");
            System.out.print("Выберите пункт: ");

            int choice = readInt();
            switch (choice) {
                case 1:
                    viewCatalog();
                    break;
                case 2:
                    addProduct();
                    break;
                case 3:
                    updateProduct();
                    break;
                case 4:
                    deleteProduct();
                    break;
                case 5:
                    filterProducts();
                    break;
                case 6:
                    auditService.printAuditLog();
                    break;
                case 7:
                    auditService.logAction(currentUser.getLogin(), "Logged out");
                    currentUser = null;
                    System.out.println("Вы вышли из своего аккаунта.");
                    return;
                default:
                    System.out.println("Неверный выбор.");
            }
        }
    }

    private void viewCatalog() {
        long startTime = System.nanoTime();
        List<Product> products = productService.getAllProducts();
        long endTime = System.nanoTime();

        System.out.println("\n--- Каталог товаров ---");
        printProductList(products);
        System.out.printf("(Запрос выполнен за %.3f мс)\n", (endTime - startTime) / 1_000_000.0);
    }

    private void addProduct() {
        System.out.print("Введите категорию: ");
        String category = scanner.nextLine();
        System.out.print("Введите бренд: ");
        String brand = scanner.nextLine();
        System.out.print("Введите цену: ");
        int price = readInt();

        Product product = new Product(category, brand, price);
        productService.addProduct(product);
        auditService.logAction(currentUser.getLogin(), "Added product: " + product);
        System.out.println("Товар успешно добавлен!");
    }

    private void updateProduct() {
        System.out.print("Введите ID товара для изменения: ");
        long id = readLong();

        if (productService.findById(id).isEmpty()) {
            System.out.println("Товар с таким ID не найден.");
            return;
        }

        System.out.print("Введите новую категорию: ");
        String category = scanner.nextLine();
        System.out.print("Введите новый бренд: ");
        String brand = scanner.nextLine();
        System.out.print("Введите новую цену: ");
        int price = readInt();

        if (productService.updateProduct(id, category, brand, price)) {
            auditService.logAction(currentUser.getLogin(), "Updated product with ID: " + id);
            System.out.println("Товар успешно обновлен.");
        }
    }

    private void deleteProduct() {
        System.out.print("Введите ID товара для удаления: ");
        long id = readLong();
        if (productService.deleteProduct(id)) {
            auditService.logAction(currentUser.getLogin(), "Deleted product with ID: " + id);
            System.out.println("Товар успешно удален.");
        } else {
            System.out.println("Товар с таким ID не найден.");
        }
    }

    private void filterProducts() {
        System.out.println("Фильтровать по:");
        System.out.println("1. Категории");
        System.out.println("2. Бренду");
        System.out.print("Выберите тип фильтра: ");
        int choice = readInt();

        String filterType = "";
        String value = "";

        switch(choice) {
            case 1:
                filterType = "category";
                System.out.print("Введите название категории: ");
                value = scanner.nextLine();
                break;
            case 2:
                filterType = "brand";
                System.out.print("Введите название бренда: ");
                value = scanner.nextLine();
                break;
            default:
                System.out.println("Неверный тип фильтра.");
                return;
        }

        long startTime = System.nanoTime();
        List<Product> filteredProducts = productService.filterBy(filterType, value);
        long endTime = System.nanoTime();

        System.out.println("\n--- Результаты поиска ---");
        printProductList(filteredProducts);
        System.out.printf("(Запрос выполнен за %.3f мс)\n", (endTime - startTime) / 1_000_000.0);
    }

    private void printProductList(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println("Список пуст.");
        } else {
            products.forEach(System.out::println);
        }
    }

    private int readInt() {
        while (true) {
            try {
                int result = Integer.parseInt(scanner.nextLine());
                return result;
            } catch (NumberFormatException e) {
                System.out.print("Ошибка! Введите целое число: ");
            }
        }
    }

    private long readLong() {
        while (true) {
            try {
                long result = Long.parseLong(scanner.nextLine());
                return result;
            } catch (NumberFormatException e) {
                System.out.print("Ошибка! Введите целое число (long): ");
            }
        }
    }
}