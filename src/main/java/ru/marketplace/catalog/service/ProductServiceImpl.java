package ru.marketplace.catalog.service;

import ru.marketplace.catalog.model.Product;
import ru.marketplace.catalog.repository.InMemoryProductRepository;
import ru.marketplace.catalog.repository.ProductRepository;

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
        productRepository.save(product);
        invalidateCache();
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> findById(long id) {
        return productRepository.findById(id);
    }

    @Override
    public boolean updateProduct(long id, String newCategory, String newBrand, int newPrice) {
        Optional<Product> productOpt = findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setCategory(newCategory);
            product.setBrand(newBrand);
            product.setPrice(newPrice);
            productRepository.save(product);
            invalidateCache();
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteProduct(long id) {
        if (productRepository.deleteById(id)) {
            invalidateCache();
            return true;
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

        List<Product> result;
        switch (filterType) {
            case "category" -> result = productRepository.findAll().stream()
                    .filter(p -> p.getCategory().equalsIgnoreCase(value))
                    .collect(Collectors.toList());
            case "brand" -> result = productRepository.findAll().stream()
                    .filter(p -> p.getBrand().equalsIgnoreCase(value))
                    .collect(Collectors.toList());
            default -> result = List.of();
        }

        cache.put(cacheKey, result);
        return result;
    }

    private void invalidateCache() {
        cache.clear();
        System.out.println("(Cache invalidated)");
    }
}