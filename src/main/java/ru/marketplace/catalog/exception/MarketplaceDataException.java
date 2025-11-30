package ru.marketplace.catalog.exception;

/**
 * Кастомное исключение для ошибок доступа к данным.
 */
public class MarketplaceDataException extends RuntimeException {

    public MarketplaceDataException(String message) {
        super(message);
    }

    public MarketplaceDataException(String message, Throwable cause) {
        super(message, cause);
    }
}