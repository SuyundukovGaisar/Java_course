package ru.marketplace.catalog.repository;

import ru.marketplace.catalog.model.User;
import java.util.Optional;

/**
 * Интерфейс репозитория для работы с пользователями.
 */
public interface UserRepository {
    void save(User user);
    Optional<User> findByLogin(String login);
    boolean existsByLogin(String login);
}