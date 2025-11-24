package ru.marketplace.catalog.repository;

import ru.marketplace.catalog.model.Product;
import ru.marketplace.catalog.exception.RepositoryException;
import java.util.List;
import java.util.Optional;

/**
 * Интерфейс, описывающий контракт для хранилища продуктов (Data Access Layer).
 * Определяет основные CRUD-операции для работы с сущностями Product.
 */
public interface ProductRepository {
    /**
     * Сохраняет или обновляет продукт в хранилище.
     * @param product продукт для сохранения.
     */
    void save(Product product) throws RepositoryException;
    /**
     * Возвращает все продукты из хранилища.
     * @return список всех продуктов.
     */
    List<Product> findAll() throws RepositoryException;
    /**
     * Ищет продукт по его уникальному идентификатору.
     * @param id идентификатор продукта.
     * @return {@link Optional} с продуктом, если найден, иначе пустой {@link Optional}.
     */
    Optional<Product> findById(long id) throws RepositoryException;
    /**
     * Удаляет продукт по его уникальному идентификатору.
     * @param id идентификатор продукта.
     * @return true, если продукт был удален, иначе false.
     */
    boolean deleteById(long id) throws RepositoryException;
    /**
     * Проверяет существование продукта по его уникальному идентификатору.
     * @param id идентификатор продукта.
     * @return true, если продукт существует, иначе false.
     */
    boolean existsById(long id) throws RepositoryException;
}