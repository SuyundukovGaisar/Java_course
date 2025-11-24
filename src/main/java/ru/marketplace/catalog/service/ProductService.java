package ru.marketplace.catalog.service;

import ru.marketplace.catalog.model.Product;
import java.util.List;
import java.util.Optional;

/**
 * Интерфейс, описывающий контракт для сервиса управления продуктами.
 */

public interface ProductService {
    /**
     * Добавляет новый продукт в каталог.
     * @param product объект продукта для добавления.
     */
    void addProduct(Product product);

    /**
     * Возвращает список всех продуктов в каталоге.
     * @return список всех продуктов.
     */
    List<Product> getAllProducts();


    Optional<Product> findById(long id);
    boolean updateProduct(long id, String newCategory, String newBrand, int newPrice);
    boolean deleteProduct(long id);
    List<Product> filterBy(String filterType, String value);
}