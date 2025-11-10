package ru.marketplace.catalog.service;

import ru.marketplace.catalog.model.User;
import java.util.Optional;

public interface UserService {
    boolean registerUser(String login, String password);
    Optional<User> loginUser(String login, String password);
}