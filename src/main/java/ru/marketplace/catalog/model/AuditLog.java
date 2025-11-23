package ru.marketplace.catalog.model;

import java.time.LocalDateTime;

public class AuditLog {
    private Long id;
    private LocalDateTime timestamp;
    private String username;
    private String action;

    public AuditLog(Long id, LocalDateTime timestamp, String username, String action) {
        this.id = id;
        this.timestamp = timestamp;
        this.username = username;
        this.action = action;
    }

    public AuditLog(String username, String action) {
        this.timestamp = LocalDateTime.now();
        this.username = username;
        this.action = action;
    }

    public Long getId() { return id; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getUsername() { return username; }
    public String getAction() { return action; }

    @Override
    public String toString() {
        return String.format("[%s] User '%s': %s", timestamp, username, action);
    }
}