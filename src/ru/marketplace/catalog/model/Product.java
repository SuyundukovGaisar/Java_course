package ru.marketplace.catalog.model;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;



public class Product {
    private static final AtomicLong idGenerator = new AtomicLong(0);

    private final long id;
    private String category;
    private String brand;
    private int price;

    public Product(String category, String brand, int price) {
        this.id = idGenerator.incrementAndGet();
        this.category = category;
        this.brand = brand;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getBrand() {
        return brand;
    }

    public int getPrice() {
        return price;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ru.marketplace.catalog.model.Product{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", brand='" + brand + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}