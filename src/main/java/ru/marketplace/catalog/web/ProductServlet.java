package ru.marketplace.catalog.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.marketplace.catalog.dto.ProductDto;
import ru.marketplace.catalog.mapper.ProductMapper;
import ru.marketplace.catalog.model.Product;
import ru.marketplace.catalog.service.ProductService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервлет для управления продуктами.
 * Обрабатывает запросы по пути /products/*
 */
@WebServlet(name = "ProductServlet", urlPatterns = "/products/*")
public class ProductServlet extends HttpServlet {

    private final ProductService productService;

    public ProductServlet(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Обрабатывает GET запросы.
     * GET /products - получить все продукты.
     * GET /products/{id} - получить продукт по ID.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            List<Product> products = productService.getAllProducts();
            List<ProductDto> dtos = products.stream()
                    .map(ProductMapper.INSTANCE::toDto)
                    .collect(Collectors.toList());
            JsonUtils.writeJson(resp, dtos);
        } else {
            try {
                long id = Long.parseLong(pathInfo.substring(1));
                productService.findById(id).ifPresentOrElse(
                        product -> {
                            try {
                                JsonUtils.writeJson(resp, ProductMapper.INSTANCE.toDto(product));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        },
                        () -> resp.setStatus(HttpServletResponse.SC_NOT_FOUND) // 404
                );
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format"); // 400
            }
        }
    }

    /**
     * Обрабатывает POST запросы.
     * POST /products - создать новый продукт.
     * Ожидает JSON в теле запроса.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        try {
            ProductDto dto = JsonUtils.readJson(req, ProductDto.class);

            String validationError = ValidationUtils.validate(dto);
            if (validationError != null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, validationError);
                return;
            }

            Product product = ProductMapper.INSTANCE.toEntity(dto);
            productService.addProduct(product);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            JsonUtils.writeJson(resp, ProductMapper.INSTANCE.toDto(product));
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON data");
        }
    }

    /**
     * Обрабатывает DELETE запросы.
     * DELETE /products/{id} - удалить продукт.
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID is required");
            return;
        }

        try {
            long id = Long.parseLong(pathInfo.substring(1));
            if (productService.deleteProduct(id)) {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format");
        }
    }
}