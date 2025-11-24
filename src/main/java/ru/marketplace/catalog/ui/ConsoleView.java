package ru.marketplace.catalog.ui;

import ru.marketplace.catalog.model.Product;
import java.util.List;
import java.util.Scanner;

/**
 * Класс, отвечающий за взаимодействие с пользователем через консоль (слой View).
 * Не содержит бизнес-логики, только отображение информации и чтение ввода.
 */
public class ConsoleView {
    private final Scanner scanner;

    public ConsoleView(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Отображает сообщение для пользователя.
     * @param message текст сообщения.
     */
    public void showMessage(String message) {
        System.out.println(message);
    }

    public int promptForMenuChoice(String menuText) {
        System.out.print(menuText);
        return readInt();
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        return readInt();
    }

    public long promptForLong(String prompt) {
        System.out.print(prompt);
        return readLong();
    }

    public void displayProducts(List<Product> products) {
        System.out.println("\n--- Каталог товаров ---");
        if (products.isEmpty()) {
            System.out.println("Список пуст.");
        } else {
            products.forEach(System.out::println);
        }
    }

    public void displaySearchResults(List<Product> products, double duration) {
        System.out.println("\n--- Результаты поиска ---");
        if (products.isEmpty()) {
            System.out.println("Ничего не найдено.");
        } else {
            products.forEach(System.out::println);
        }
        System.out.printf("(Запрос выполнен за %.3f мс)\n", duration);
    }

    private int readInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Ошибка! Введите целое число: ");
            }
        }
    }

    private long readLong() {
        while (true) {
            try {
                return Long.parseLong(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Ошибка! Введите целое число (long): ");
            }
        }
    }

    public void displayAuditLog(List<String> auditLog) {
        System.out.println("--- Audit Log ---");
        if (auditLog.isEmpty()) {
            System.out.println("Log is empty.");
        } else {
            auditLog.forEach(System.out::println);
        }
        System.out.println("-----------------");
    }
}