package ru.marketplace.catalog.exception;

/**
 * Исключение, выбрасываемое при ошибках на уровне доступа к данным (слой репозитория).
 * Является проверяемым исключением, чтобы заставить вызывающий код обработать ошибку.
 */
public class RepositoryException extends Exception {

    public RepositoryException(String message) {
        super(message);
    }

    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}