package ru.marketplace.catalog.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.marketplace.catalog.model.Product;
import ru.marketplace.catalog.repository.ProductRepository;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    @DisplayName("Добавление продукта должно вызывать метод репозитория")
    void addProduct_shouldCallRepository() {
        Product product = new Product("Electronics", "Sony", 100);

        productService.addProduct(product);

        verify(productRepository, times(1)).save(product);
    }

    @Test
    @DisplayName("Получение всех продуктов должно возвращать список из репозитория")
    void getAllProducts_shouldReturnList() {
        List<Product> mockList = List.of(
                new Product(1L, "Cat1", "Brand1", 100),
                new Product(2L, "Cat1", "Brand2", 200)
        );

        when(productRepository.findAll()).thenReturn(mockList);

        List<Product> result = productService.getAllProducts();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Brand1", result.get(0).getBrand());
    }

    @Test
    @DisplayName("Удаление продукта должно возвращать результат из репозитория")
    void deleteProduct_shouldReturnTrue_whenRepoReturnsTrue() {
        when(productRepository.deleteById(1L)).thenReturn(true);

        boolean result = productService.deleteProduct(1L);

        Assertions.assertTrue(result);
    }
}