package ru.marketplace.catalog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.marketplace.catalog.dto.UserDto;
import ru.marketplace.catalog.service.UserService;

/**
 * REST-контроллер для управления пользователями.
 */
@RestController
@RequestMapping("/users")
@Tag(name = "Пользователи", description = "Регистрация и аутентификация")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Регистрация нового пользователя.
     * POST /users/register
     */
    @PostMapping("/register")
    @Operation(summary = "Регистрация пользователя")
    public ResponseEntity<String> register(@RequestBody @Valid UserDto dto) {
        boolean isCreated = userService.registerUser(dto.getLogin(), dto.getPassword());
        if (isCreated) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Пользователь успешно зарегистрирован");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Пользователь с таким логином уже существует");
        }
    }

    /**
     * Вход пользователя (проверка логина и пароля).
     * POST /users/login
     */
    @PostMapping("/login")
    @Operation(summary = "Вход в систему", description = "Проверяет логин и пароль")
    public ResponseEntity<String> login(@RequestBody @Valid UserDto dto) {
        var userOpt = userService.loginUser(dto.getLogin(), dto.getPassword());

        if (userOpt.isPresent()) {
            return ResponseEntity.ok("Вход выполнен успешно! (Учетные данные верны)");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверный логин или пароль");
        }
    }
}