package ru.marketplace.catalog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO (Data Transfer Object) для передачи данных о товаре.
 * Используется в контроллерах для приема и отправки данных.
 */
public class ProductDto {

    private Long id;

    @NotBlank(message = "Категория не может быть пустой")
    private String category;

    @NotBlank(message = "Бренд не может быть пустым")
    private String brand;

    @Min(value = 1, message = "Цена должна быть больше 0")
    private int price;

    public ProductDto() {
    }

    public ProductDto(Long id, String category, String brand, int price) {
        this.id = id;
        this.category = category;
        this.brand = brand;
        this.price = price;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
}