package ru.marketplace.catalog.service.impl;

import org.springframework.stereotype.Service;
import ru.marketplace.starter.aop.annotations.Auditable;
import ru.marketplace.starter.aop.annotations.Loggable;
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
 * Реализация сервиса продуктов.
 */
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final Map<String, List<Product>> cache = new HashMap<>();

    /**
     * Конструктор для внедрения репозитория.
     */
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Loggable
    @Auditable(action = "ADD_PRODUCT")
    public void addProduct(Product product) {
        productRepository.save(product);
        invalidateCache();
    }

    @Override
    @Loggable
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> findById(long id) {
        return productRepository.findById(id);
    }

    @Override
    @Loggable
    @Auditable(action = "UPDATE_PRODUCT")
    public boolean updateProduct(long id, String newCategory, String newBrand, int newPrice) {
        Optional<Product> productOpt = productRepository.findById(id);

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
    @Loggable
    @Auditable(action = "DELETE_PRODUCT")
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

        List<Product> allProducts = productRepository.findAll();
        List<Product> result;

        switch (filterType) {
            case "category" -> result = allProducts.stream()
                    .filter(p -> p.getCategory().equalsIgnoreCase(value))
                    .collect(Collectors.toList());
            case "brand" -> result = allProducts.stream()
                    .filter(p -> p.getBrand().equalsIgnoreCase(value))
                    .collect(Collectors.toList());
            default -> result = new ArrayList<>();
        }

        cache.put(cacheKey, result);
        return result;
    }

    private void invalidateCache() {
        cache.clear();
        System.out.println("(Cache invalidated)");
    }
}