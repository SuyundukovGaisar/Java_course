package ru.marketplace.catalog.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.marketplace.catalog.dto.ProductDto;
import ru.marketplace.catalog.model.Product;
import ru.marketplace.catalog.service.ProductService;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServletTest {

    @Mock
    private ProductService productService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private ProductServlet servlet;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("GET /products должен возвращать список продуктов в JSON")
    void doGet_ShouldReturnProductList() throws Exception {
        when(request.getPathInfo()).thenReturn("/");

        Product product = new Product(1L, "Phone", "Apple", 1000);
        when(productService.getAllProducts()).thenReturn(List.of(product));

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        servlet.doGet(request, response);

        writer.flush();
        String jsonOutput = stringWriter.toString();

        assertTrue(jsonOutput.contains("Apple"));
        assertTrue(jsonOutput.contains("Phone"));

        verify(response).setContentType("application/json");
    }

    @Test
    @DisplayName("POST /products должен создавать продукт и возвращать 201")
    void doPost_ShouldCreateProduct() throws Exception {
        // Given
        String jsonInput = """
                {
                    "category": "Tablet",
                    "brand": "Samsung",
                    "price": 500
                }
                """;

        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));
        when(request.getReader()).thenReturn(reader);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        servlet.doPost(request, response);

        verify(productService, times(1)).addProduct(argThat(p ->
                p.getBrand().equals("Samsung") && p.getPrice() == 500
        ));

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }
}