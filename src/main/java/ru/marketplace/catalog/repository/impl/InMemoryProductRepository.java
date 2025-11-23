package ru.marketplace.catalog.repository.impl;

import ru.marketplace.catalog.model.Product;
import ru.marketplace.catalog.repository.ProductRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Реализация репозитория продуктов, которая хранит данные в оперативной памяти
 * с использованием {@link HashMap}.
 */
public class InMemoryProductRepository implements ProductRepository {

    private final Map<Long, Product> products = new HashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Product product) {
        products.put(product.getId(), product);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Product> findById(long id) {
        return Optional.ofNullable(products.get(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteById(long id) {
        return products.remove(id) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsById(long id) {
        return products.containsKey(id);
    }
}