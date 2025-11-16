package ru.marketplace.catalog.repository;

import ru.marketplace.catalog.db.ConnectionFactory;
import ru.marketplace.catalog.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Реализация ProductRepository с использованием JDBC для работы с PostgreSQL.
 */
public class JdbcProductRepository implements ProductRepository {

    private final ConnectionFactory connectionFactory;

    private static final String FIND_ALL_SQL = "SELECT id, category, brand, price FROM marketplace.products";
    private static final String FIND_BY_ID_SQL = "SELECT id, category, brand, price FROM marketplace.products WHERE id = ?";
    private static final String SAVE_SQL = "INSERT INTO marketplace.products (category, brand, price) VALUES (?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE marketplace.products SET category = ?, brand = ?, price = ? WHERE id = ?";
    private static final String DELETE_BY_ID_SQL = "DELETE FROM marketplace.products WHERE id = ?";
    private static final String EXISTS_BY_ID_SQL = "SELECT 1 FROM marketplace.products WHERE id = ?";

    public JdbcProductRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public void save(Product product) {
        if (product.getId() == 0) {
            try (Connection connection = connectionFactory.getConnection();
                 PreparedStatement statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {

                statement.setString(1, product.getCategory());
                statement.setString(2, product.getBrand());
                statement.setInt(3, product.getPrice());
                statement.executeUpdate();

                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    product.setId(generatedKeys.getLong(1));
                }

            } catch (SQLException e) {
                throw new RuntimeException("Ошибка при сохранении продукта", e);
            }
        } else {
            try (Connection connection = connectionFactory.getConnection();
                 PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {

                statement.setString(1, product.getCategory());
                statement.setString(2, product.getBrand());
                statement.setInt(3, product.getPrice());
                statement.setLong(4, product.getId());
                statement.executeUpdate();

            } catch (SQLException e) {
                throw new RuntimeException("Ошибка при обновлении продукта", e);
            }
        }
    }

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        try (Connection connection = connectionFactory.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(FIND_ALL_SQL)) {

            while (rs.next()) {
                products.add(mapRowToProduct(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении всех продуктов", e);
        }
        return products;
    }

    @Override
    public Optional<Product> findById(long id) {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)) {

            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToProduct(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске продукта по ID", e);
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteById(long id) {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID_SQL)) {

            statement.setLong(1, id);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении продукта", e);
        }
    }

    @Override
    public boolean existsById(long id) {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(EXISTS_BY_ID_SQL)) {

            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при проверке существования продукта", e);
        }
    }

    /**
     * Вспомогательный метод для преобразования строки из ResultSet в объект Product.
     */
    private Product mapRowToProduct(ResultSet rs) throws SQLException {
        // Теперь используем новый конструктор, который принимает ID
        return new Product(
                rs.getLong("id"),
                rs.getString("category"),
                rs.getString("brand"),
                rs.getInt("price")
        );
    }
}