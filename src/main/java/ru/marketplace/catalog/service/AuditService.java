package ru.marketplace.catalog.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для ведения журнала аудита действий пользователей.
 * Хранит лог в памяти.
 */
public class AuditService {
    private final List<String> auditLog = new ArrayList<>();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Записывает новое действие в журнал аудита.
     * @param username имя пользователя, совершившего действие.
     * @param action   описание действия.
     */
    public void logAction(String username, String action) {
        String timestamp = LocalDateTime.now().format(formatter);
        auditLog.add(String.format("[%s] ru.marketplace.catalog.model.User '%s' performed action: %s", timestamp, username, action));
    }

    /**
     * Выводит весь журнал аудита в стандартный поток вывода.
     */
    public void printAuditLog() {
        System.out.println("--- Audit Log ---");
        if (auditLog.isEmpty()) {
            System.out.println("Log is empty.");
        } else {
            auditLog.forEach(System.out::println);
        }
        System.out.println("-----------------");
    }
}