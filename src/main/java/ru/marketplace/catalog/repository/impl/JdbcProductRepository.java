package ru.marketplace.catalog.repository.impl;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.marketplace.catalog.exception.MarketplaceDataException;
import ru.marketplace.catalog.model.Product;
import ru.marketplace.catalog.repository.ProductRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Реализация репозитория товаров с использованием Spring JDBC.
 * Выполняет SQL-запросы к PostgreSQL через {@link NamedParameterJdbcTemplate}.
 */
@Repository
public class JdbcProductRepository implements ProductRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    /**
     * Конструктор для внедрения зависимостей.
     *
     * @param jdbcTemplate шаблон Spring JDBC для выполнения запросов с именованными параметрами.
     */
    public JdbcProductRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Маппер для преобразования строки ResultSet в объект Product.
     */
    private static final RowMapper<Product> PRODUCT_ROW_MAPPER = (rs, rowNum) -> new Product(
            rs.getLong("id"),
            rs.getString("category"),
            rs.getString("brand"),
            rs.getInt("price")
    );

    @Override
    public void save(Product product) {
        try {
            if (product.getId() == 0) {
                String sql = "INSERT INTO marketplace.products (category, brand, price) VALUES " +
                        "(:category, :brand, :price)";
                MapSqlParameterSource params = new MapSqlParameterSource()
                        .addValue("category", product.getCategory())
                        .addValue("brand", product.getBrand())
                        .addValue("price", product.getPrice());

                KeyHolder keyHolder = new GeneratedKeyHolder();
                jdbcTemplate.update(sql, params, keyHolder, new String[]{"id"});

                // Устанавливаем сгенерированный ID обратно в объект
                Number key = keyHolder.getKey();
                if (key != null) {
                    product.setId(key.longValue());
                }
            } else {
                String sql = "UPDATE marketplace.products SET category = :category, brand = :brand, " +
                        "price = :price WHERE id = :id";
                MapSqlParameterSource params = new MapSqlParameterSource()
                        .addValue("id", product.getId())
                        .addValue("category", product.getCategory())
                        .addValue("brand", product.getBrand())
                        .addValue("price", product.getPrice());
                jdbcTemplate.update(sql, params);
            }
        } catch (DataAccessException e) {
            throw new MarketplaceDataException("Ошибка при сохранении продукта: " + product, e);
        }
    }

    @Override
    public List<Product> findAll() {
        try {
            String sql = "SELECT id, category, brand, price FROM marketplace.products";
            return jdbcTemplate.query(sql, PRODUCT_ROW_MAPPER);
        } catch (DataAccessException e) {
            throw new MarketplaceDataException("Ошибка при получении списка продуктов", e);
        }
    }

    @Override
    public Optional<Product> findById(long id) {
        try {
            String sql = "SELECT id, category, brand, price FROM marketplace.products WHERE id = :id";
            List<Product> products = jdbcTemplate.query(sql, new MapSqlParameterSource("id", id),
                    PRODUCT_ROW_MAPPER);
            return products.stream().findFirst();
        } catch (DataAccessException e) {
            throw new MarketplaceDataException("Ошибка при поиске продукта с ID: " + id, e);
        }
    }

    @Override
    public boolean deleteById(long id) {
        try {
            String sql = "DELETE FROM marketplace.products WHERE id = :id";
            int rowsAffected = jdbcTemplate.update(sql, new MapSqlParameterSource("id", id));
            return rowsAffected > 0;
        } catch (DataAccessException e) {
            throw new MarketplaceDataException("Ошибка при удалении продукта с ID: " + id, e);
        }
    }

    @Override
    public boolean existsById(long id) {
        try {
            String sql = "SELECT count(*) FROM marketplace.products WHERE id = :id";
            Integer count = jdbcTemplate.queryForObject(sql, new MapSqlParameterSource("id", id),
                    Integer.class);
            return count != null && count > 0;
        } catch (DataAccessException e) {
            throw new MarketplaceDataException("Ошибка при проверке существования продукта с ID: " + id, e);
        }
    }
}