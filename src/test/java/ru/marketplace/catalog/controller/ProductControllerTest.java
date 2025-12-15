package ru.marketplace.catalog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.marketplace.catalog.dto.ProductDto;
import ru.marketplace.catalog.mapper.ProductMapper;
import ru.marketplace.catalog.mapper.ProductMapperImpl;
import ru.marketplace.catalog.model.Product;
import ru.marketplace.catalog.service.ProductService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Тест контроллера ProductController.
 * Использует @WebMvcTest для тестирования только WEB-слоя.
 */
@WebMvcTest(ProductController.class)
@Import(ProductMapperImpl.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllProducts_ShouldReturnList() throws Exception {
        Product product = new Product(1L, "Electronics", "Sony", 100);
        when(productService.getAllProducts()).thenReturn(List.of(product));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].brand").value("Sony"))
                .andExpect(jsonPath("$[0].price").value(100));
    }

    @Test
    void getProductById_ShouldReturnProduct_WhenExists() throws Exception {
        long id = 1L;
        Product product = new Product(id, "Electronics", "Sony", 100);
        when(productService.findById(id)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/products/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void getProductById_ShouldReturn404_WhenNotExists() throws Exception {
        long id = 999L;
        when(productService.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/products/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void createProduct_ShouldReturnCreated() throws Exception {
        ProductDto dto = new ProductDto(null, "Electronics", "LG", 200);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.brand").value("LG"));
    }

    @Test
    void createProduct_ShouldReturnBadRequest_WhenInvalid() throws Exception {
        ProductDto dto = new ProductDto(null, "Electronics", "LG", -200);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}