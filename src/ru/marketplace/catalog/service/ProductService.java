package ru.marketplace.catalog.service;

import ru.marketplace.catalog.model.Product;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProductService {
    private final Map<Long, Product> products = new HashMap<>();
    private final Map<String, List<Product>> cache = new HashMap<>();

    public void addProduct(Product product) {
        products.put(product.getId(), product);
        invalidateCache();
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }

    public Optional<Product> findById(long id) {
        return Optional.ofNullable(products.get(id));
    }

    public boolean updateProduct(long id, String newCategory, String newBrand, int newPrice) {
        Optional<Product> productOpt = findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setCategory(newCategory);
            product.setBrand(newBrand);
            product.setPrice(newPrice);
            invalidateCache();
            return true;
        }
        return false;
    }

    public boolean deleteProduct(long id) {
        if (products.remove(id) != null) {
            invalidateCache();
            return true;
        }
        return false;
    }

    public List<Product> filterBy(String filterType, String value) {
        String cacheKey = filterType + ":" + value;
        if (cache.containsKey(cacheKey)) {
            System.out.println(" (Fetching from cache)");
            return cache.get(cacheKey);
        }

        List<Product> result = new ArrayList<>();
        switch (filterType) {
            case "category":
                result = products.values().stream()
                        .filter(p -> p.getCategory().equalsIgnoreCase(value))
                        .collect(Collectors.toList());
                break;
            case "brand":
                result = products.values().stream()
                        .filter(p -> p.getBrand().equalsIgnoreCase(value))
                        .collect(Collectors.toList());
                break;
        }

        cache.put(cacheKey, result);
        return result;
    }

    private void invalidateCache() {
        cache.clear();
        System.out.println("(Cache invalidated)");
    }
}