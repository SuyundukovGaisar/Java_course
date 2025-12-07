package ru.marketplace.catalog.service;

import ru.marketplace.catalog.model.Product;
import java.util.List;
import java.util.Optional;

/**
 * Интерфейс сервиса управления товарами.
 * Определяет бизнес-логику работы с каталогом.
 */
public interface ProductService {

    /**
     * Добавляет новый продукт в каталог.
     *
     * @param product объект продукта для добавления.
     */
    void addProduct(Product product);

    /**
     * Возвращает список всех продуктов.
     *
     * @return список продуктов.
     */
    List<Product> getAllProducts();

    /**
     * Ищет продукт по ID.
     *
     * @param id идентификатор продукта.
     * @return Optional с продуктом, если найден.
     */
    Optional<Product> findById(long id);

    /**
     * Обновляет информацию о продукте.
     *
     * @param id          ID продукта.
     * @param newCategory новая категория.
     * @param newBrand    новый бренд.
     * @param newPrice    новая цена.
     * @return true, если обновление прошло успешно, иначе false.
     */
    boolean updateProduct(long id, String newCategory, String newBrand, int newPrice);

    /**
     * Удаляет продукт по ID.
     *
     * @param id ID продукта.
     * @return true, если удаление успешно.
     */
    boolean deleteProduct(long id);

    /**
     * Фильтрует продукты по заданным критериям.
     *
     * @param filterType тип фильтра (category, brand).
     * @param value      значение фильтра.
     * @return список отфильтрованных продуктов.
     */
    List<Product> filterBy(String filterType, String value);
}