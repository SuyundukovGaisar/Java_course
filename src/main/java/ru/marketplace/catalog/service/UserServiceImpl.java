package ru.marketplace.catalog.service;

import ru.marketplace.catalog.model.User;
import ru.marketplace.catalog.repository.UserRepository;

import java.util.Optional;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean registerUser(String login, String password) {
        if (userRepository.existsByLogin(login)) {
            return false;
        }

        User newUser = new User(login, password);
        userRepository.save(newUser);
        return true;
    }

    @Override
    public Optional<User> loginUser(String login, String password) {
        Optional<User> userOpt = userRepository.findByLogin(login);

        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            return userOpt;
        }

        return Optional.empty();
    }
}