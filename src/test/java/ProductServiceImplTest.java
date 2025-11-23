import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.marketplace.catalog.model.Product;
import ru.marketplace.catalog.repository.impl.InMemoryProductRepository;
import ru.marketplace.catalog.repository.ProductRepository;
import ru.marketplace.catalog.service.ProductService;
import ru.marketplace.catalog.service.impl.ProductServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class ProductServiceImplTest {

    private ProductService productService;
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        ProductRepository dummyRepo = new ProductRepository() {
            @Override
            public void save(Product product) {}

            @Override
            public List<Product> findAll() { return new ArrayList<>(); }

            @Override
            public Optional<Product> findById(long id) { return Optional.empty(); }

            @Override
            public boolean deleteById(long id) { return true; }

            @Override
            public boolean existsById(long id) { return false; }
        };

        productService = new ProductServiceImpl(dummyRepo);
    }

    @Test
    @DisplayName("Добавление продукта должно увеличивать общее количество продуктов")
    void addProduct_shouldIncreaseProductCount() {
        Product phone = new Product("Electronics", "Apple", 1000);
        int initialSize = productService.getAllProducts().size();

        productService.addProduct(phone);
        int finalSize = productService.getAllProducts().size();

        Assertions.assertEquals(0, initialSize, "Изначально список должен быть пуст");
        Assertions.assertEquals(1, finalSize, "После добавления в списке должен быть один продукт");
    }

    @Test
    @DisplayName("Удаление существующего продукта должно вернуть true и уменьшить количество")
    void deleteProduct_shouldReturnTrue_forExistingProduct() {
        Product laptop = new Product("Electronics", "Dell", 1500);
        productService.addProduct(laptop);
        long productId = laptop.getId();

        boolean result = productService.deleteProduct(productId);
        int finalSize = productService.getAllProducts().size();

        Assertions.assertTrue(result, "Удаление существующего продукта должно вернуть true");
        Assertions.assertEquals(0, finalSize, "После удаления список должен быть пуст");
    }

    @Test
    @DisplayName("Удаление несуществующего продукта должно вернуть false")
    void deleteProduct_shouldReturnFalse_forNonExistentProduct() {
        long nonExistentId = 999L;

        boolean result = productService.deleteProduct(nonExistentId);

        Assertions.assertFalse(result, "Удаление несуществующего продукта должно вернуть false");
    }

    @Test
    @DisplayName("Фильтрация по категории должна возвращать только релевантные продукты")
    void filterBy_category_shouldReturnMatchingProducts() {
        productService.addProduct(new Product("Electronics", "Apple", 1000));
        productService.addProduct(new Product("Books", "Author House", 20));
        productService.addProduct(new Product("Electronics", "Samsung", 800));

        List<Product> electronics = productService.filterBy("category", "Electronics");
        List<Product> books = productService.filterBy("category", "books");

        Assertions.assertEquals(2, electronics.size(), "Должно быть найдено 2 товара в категории Electronics");
        Assertions.assertEquals(1, books.size(), "Должна быть найдена 1 книга");
    }
}