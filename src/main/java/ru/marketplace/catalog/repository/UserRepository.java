package ru.marketplace.catalog.repository;

import ru.marketplace.catalog.model.User;
import java.util.Optional;

/**
 * Интерфейс, описывающий контракт для хранилища пользователей.
 */
public interface UserRepository {

    /**
     * Сохраняет нового пользователя в БД.
     * @param user пользователь для сохранения.
     */
    void save(User user);

    /**
     * Ищет пользователя по его логину.
     * @param login логин пользователя.
     * @return {@link Optional} с пользователем, если найден, иначе пустой {@link Optional}.
     */
    Optional<User> findByLogin(String login);

    /**
     * Проверяет, существует ли пользователь с таким логином.
     * @param login логин для проверки.
     * @return true, если пользователь существует, иначе false.
     */
    boolean existsByLogin(String login);
}