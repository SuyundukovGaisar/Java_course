package ru.marketplace.catalog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.marketplace.catalog.dto.ProductDto;
import ru.marketplace.catalog.mapper.ProductMapper;
import ru.marketplace.catalog.model.Product;
import ru.marketplace.catalog.service.ProductService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST-контроллер для управления товарами.
 * Обрабатывает запросы по пути /products.
 */
@RestController
@RequestMapping("/products")
@Tag(name = "Товары", description = "API для управления каталогом товаров")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    /**
     * Конструктор для внедрения зависимостей.
     */
    public ProductController(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    /**
     * Получить список всех товаров.
     * GET /products
     */
    @GetMapping
    @Operation(summary = "Получить все товары", description = "Возвращает полный список товаров из каталога")
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts().stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Получить товар по ID.
     * GET /products/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Получить товар по ID")
    public ResponseEntity<ProductDto> getProductById(@PathVariable long id) {
        return productService.findById(id)
                .map(product -> ResponseEntity.ok(productMapper.toDto(product)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Создать новый товар.
     * POST /products
     */
    @PostMapping
    @Operation(summary = "Добавить новый товар")
    public ResponseEntity<ProductDto> createProduct(@RequestBody @Valid ProductDto dto) {
        Product product = productMapper.toEntity(dto);
        productService.addProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productMapper.toDto(product));
    }

    /**
     * Обновить существующий товар.
     * PUT /products/{id}
     */
    @PutMapping("/{id}")
    @Operation(summary = "Обновить товар", description = "Полное обновление полей товара")
    public ResponseEntity<Void> updateProduct(@PathVariable long id, @RequestBody @Valid ProductDto dto) {
        boolean updated = productService.updateProduct(id, dto.getCategory(), dto.getBrand(), dto.getPrice());
        if (updated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Удалить товар.
     * DELETE /products/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить товар")
    public ResponseEntity<Void> deleteProduct(@PathVariable long id) {
        if (productService.deleteProduct(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Поиск товаров (фильтрация).
     * GET /products/search?type=brand&value=Apple
     */
    @GetMapping("/search")
    @Operation(summary = "Поиск товаров", description = "Фильтрация по типу (category/brand) и значению")
    public List<ProductDto> searchProducts(@RequestParam String type, @RequestParam String value) {
        return productService.filterBy(type, value).stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }
}