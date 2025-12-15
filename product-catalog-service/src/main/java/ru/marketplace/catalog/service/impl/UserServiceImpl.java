package ru.marketplace.catalog.service.impl;

import org.springframework.stereotype.Service;
import ru.marketplace.starter.aop.annotations.Auditable;
import ru.marketplace.starter.aop.annotations.Loggable;
import ru.marketplace.catalog.model.User;
import ru.marketplace.catalog.repository.UserRepository;
import ru.marketplace.catalog.service.UserService;

import java.util.Optional;

/**
 * Реализация сервиса пользователей.
 * Управляет регистрацией и входом.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Loggable
    @Auditable(action = "REGISTER_USER")
    public boolean registerUser(String login, String password) {
        if (userRepository.existsByLogin(login)) {
            return false;
        }

        User newUser = new User(login, password);
        userRepository.save(newUser);
        return true;
    }

    @Override
    @Loggable
    @Auditable(action = "LOGIN_USER")
    public Optional<User> loginUser(String login, String password) {
        Optional<User> userOpt = userRepository.findByLogin(login);

        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            return userOpt;
        }
        return Optional.empty();
    }
}