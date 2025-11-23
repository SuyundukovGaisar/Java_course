package ru.marketplace.catalog.repository;

import ru.marketplace.catalog.exception.RepositoryException;
import ru.marketplace.catalog.model.User;
import java.util.Optional;

/**
 * Интерфейс, описывающий контракт для хранилища пользователей.
 * Определяет операции доступа к данным пользователей.
 */
public interface UserRepository {

    /**
     * Сохраняет нового пользователя в БД.
     *
     * @param user пользователь для сохранения.
     * @throws RepositoryException если произошла ошибка доступа к данным (SQL ошибка).
     */
    void save(User user) throws RepositoryException;

    /**
     * Ищет пользователя по его логину.
     *
     * @param login логин пользователя.
     * @return {@link Optional} с пользователем, если найден, иначе пустой {@link Optional}.
     * @throws RepositoryException если произошла ошибка доступа к данным.
     */
    Optional<User> findByLogin(String login) throws RepositoryException;

    /**
     * Проверяет, существует ли пользователь с таким логином.
     *
     * @param login логин для проверки.
     * @return true, если пользователь существует, иначе false.
     * @throws RepositoryException если произошла ошибка доступа к данным.
     */
    boolean existsByLogin(String login) throws RepositoryException;
}