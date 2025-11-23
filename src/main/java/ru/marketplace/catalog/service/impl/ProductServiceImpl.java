package ru.marketplace.catalog.service.impl;

import ru.marketplace.catalog.exception.RepositoryException;
import ru.marketplace.catalog.model.Product;
import ru.marketplace.catalog.repository.ProductRepository;
import ru.marketplace.catalog.service.ProductService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Стандартная реализация сервиса для управления продуктами.
 * Содержит бизнес-логику и использует репозиторий для доступа к данным.
 */
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final Map<String, List<Product>> cache = new HashMap<>();

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void addProduct(Product product) {
        try {
            productRepository.save(product);
            invalidateCache();
        } catch (RepositoryException e) {
            System.err.println("Ошибка при добавлении продукта: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<Product> getAllProducts() {
        try {
            return productRepository.findAll();
        } catch (RepositoryException e) {
            System.err.println("Ошибка при получении списка продуктов: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>(); // Возвращаем пустой список при ошибке
        }
    }

    @Override
    public Optional<Product> findById(long id) {
        try {
            return productRepository.findById(id);
        } catch (RepositoryException e) {
            System.err.println("Ошибка при поиске продукта по ID: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public boolean updateProduct(long id, String newCategory, String newBrand, int newPrice) {
        // Здесь мы используем this.findById, который уже обрабатывает исключение внутри себя
        Optional<Product> productOpt = findById(id);

        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setCategory(newCategory);
            product.setBrand(newBrand);
            product.setPrice(newPrice);

            try {
                productRepository.save(product);
                invalidateCache();
                return true;
            } catch (RepositoryException e) {
                System.err.println("Ошибка при обновлении продукта: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean deleteProduct(long id) {
        try {
            if (productRepository.deleteById(id)) {
                invalidateCache();
                return true;
            }
        } catch (RepositoryException e) {
            System.err.println("Ошибка при удалении продукта: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Product> filterBy(String filterType, String value) {
        String cacheKey = filterType + ":" + value;
        if (cache.containsKey(cacheKey)) {
            System.out.println(" (Fetching from cache)");
            return cache.get(cacheKey);
        }

        try {
            List<Product> allProducts = productRepository.findAll();
            List<Product> result;

            switch (filterType) {
                case "category" -> result = allProducts.stream()
                        .filter(p -> p.getCategory().equalsIgnoreCase(value))
                        .collect(Collectors.toList());
                case "brand" -> result = allProducts.stream()
                        .filter(p -> p.getBrand().equalsIgnoreCase(value))
                        .collect(Collectors.toList());
                default -> result = List.of();
            }

            cache.put(cacheKey, result);
            return result;
        } catch (RepositoryException e) {
            System.err.println("Ошибка при фильтрации продуктов: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void invalidateCache() {
        cache.clear();
        System.out.println("(Cache invalidated)");
    }
}