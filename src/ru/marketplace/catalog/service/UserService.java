package ru.marketplace.catalog.service;

import ru.marketplace.catalog.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserService {
    private final Map<String, User> users = new HashMap<>();

    public boolean registerUser(String login, String password) {
        if (users.containsKey(login)) {
            return false;
        }
        users.put(login, new User(login, password));
        return true;
    }

    public Optional<User> loginUser(String login, String password) {
        User user = users.get(login);
        if (user != null && user.getPassword().equals(password)) {
            return Optional.of(user);
        }
        return Optional.empty();
    }
}