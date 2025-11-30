package ru.marketplace.catalog.repository;

import ru.marketplace.catalog.model.Product;
import java.util.List;
import java.util.Optional;

/**
 * Интерфейс репозитория для работы с товарами.
 */
public interface ProductRepository {
    void save(Product product);
    List<Product> findAll();
    Optional<Product> findById(long id);
    boolean deleteById(long id);
    boolean existsById(long id);
}