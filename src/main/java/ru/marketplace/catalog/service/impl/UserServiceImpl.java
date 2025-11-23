package ru.marketplace.catalog.service.impl;

import ru.marketplace.catalog.exception.RepositoryException;
import ru.marketplace.catalog.model.User;
import ru.marketplace.catalog.repository.UserRepository;
import ru.marketplace.catalog.service.UserService;

import java.util.Optional;

/**
 * Реализация сервиса управления пользователями.
 * Обеспечивает регистрацию и вход в систему.
 */
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Регистрирует нового пользователя.
     *
     * @param login    логин.
     * @param password пароль.
     * @return true, если регистрация успешна, false, если пользователь уже существует или произошла ошибка БД.
     */
    @Override
    public boolean registerUser(String login, String password) {
        try {
            if (userRepository.existsByLogin(login)) {
                return false;
            }

            User newUser = new User(login, password);
            userRepository.save(newUser);
            return true;

        } catch (RepositoryException e) {
            System.err.println("Ошибка БД при регистрации пользователя: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Выполняет вход пользователя в систему.
     *
     * @param login    логин.
     * @param password пароль.
     * @return Optional с пользователем, если логин и пароль верны.
     */
    @Override
    public Optional<User> loginUser(String login, String password) {
        try {
            Optional<User> userOpt = userRepository.findByLogin(login);

            if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
                return userOpt;
            }
        } catch (RepositoryException e) {
            System.err.println("Ошибка БД при входе пользователя: " + e.getMessage());
            e.printStackTrace();
        }

        return Optional.empty();
    }
}