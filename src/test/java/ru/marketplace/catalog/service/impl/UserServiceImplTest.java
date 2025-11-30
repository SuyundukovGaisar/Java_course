package ru.marketplace.catalog.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.marketplace.catalog.model.User;
import ru.marketplace.catalog.repository.UserRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Регистрация успешна, если пользователя еще нет")
    void registerUser_shouldSucceed_whenLoginIsUnique() {
        String login = "newUser";
        String password = "123";

        when(userRepository.existsByLogin(login)).thenReturn(false);

        boolean result = userService.registerUser(login, password);

        Assertions.assertTrue(result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Регистрация провалена, если пользователь уже есть")
    void registerUser_shouldFail_whenLoginExists() {
        String login = "existingUser";

        when(userRepository.existsByLogin(login)).thenReturn(true);

        boolean result = userService.registerUser(login, "123");

        Assertions.assertFalse(result);
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Логин успешен при правильном пароле")
    void loginUser_shouldSucceed_withCorrectCredentials() {
        String login = "user";
        String password = "pass";
        User userFromDb = new User(login, password);

        when(userRepository.findByLogin(login)).thenReturn(Optional.of(userFromDb));

        Optional<User> result = userService.loginUser(login, password);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(login, result.get().getLogin());
    }
}