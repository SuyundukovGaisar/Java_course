package ru.marketplace.catalog.service;

import ru.marketplace.catalog.model.User;
import java.util.Optional;

/**
 * Интерфейс сервиса управления пользователями.
 * Отвечает за регистрацию и аутентификацию.
 */
public interface UserService {

    /**
     * Регистрирует нового пользователя.
     *
     * @param login    логин.
     * @param password пароль.
     * @return true, если пользователь успешно создан.
     */
    boolean registerUser(String login, String password);

    /**
     * Проверяет учетные данные пользователя.
     *
     * @param login    логин.
     * @param password пароль.
     * @return Optional с пользователем, если вход успешен.
     */
    Optional<User> loginUser(String login, String password);
}