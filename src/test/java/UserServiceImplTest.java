import org.junit.jupiter.api.*;
import ru.marketplace.catalog.model.User;
import ru.marketplace.catalog.repository.UserRepository;
import ru.marketplace.catalog.service.UserService;
import ru.marketplace.catalog.service.impl.UserServiceImpl;

import java.util.Optional;

class UserServiceImplTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        UserRepository dummyRepo = new UserRepository() {
            @Override
            public void save(User user) {
                // Ничего не делаем, это просто тест
            }

            @Override
            public Optional<User> findByLogin(String login) {
                return Optional.empty();
            }

            @Override
            public boolean existsByLogin(String login) {
                return false;
            }
        };

        userService = new UserServiceImpl(dummyRepo);
    }

    @Test
    @DisplayName("Регистрация нового пользователя должна проходить успешно")
    void registerUser_shouldSucceed_whenLoginIsUnique() {
        String login = "newUser";
        String password = "password123";

        boolean result = userService.registerUser(login, password);

        Assertions.assertTrue(result, "Метод должен вернуть true при успешной регистрации");
    }

    @Test
    @DisplayName("Регистрация пользователя с существующим логином должна провалиться")
    void registerUser_shouldFail_whenLoginExists() {
        String existingLogin = "existingUser";
        String password = "password123";
        userService.registerUser(existingLogin, password);

        boolean result = userService.registerUser(existingLogin, "anotherPassword");

        Assertions.assertFalse(result, "Метод должен вернуть false при попытке регистрации дубликата");
    }

    @Test
    @DisplayName("Вход с верными данными должен быть успешным")
    void loginUser_shouldSucceed_withCorrectCredentials() {
        String login = "testUser";
        String password = "password";
        userService.registerUser(login, password);

        Optional<User> result = userService.loginUser(login, password);

        Assertions.assertTrue(result.isPresent(), "Optional не должен быть пустым");
        Assertions.assertEquals(login, result.get().getLogin(), "Логин пользователя должен совпадать");
    }

    @Test
    @DisplayName("Вход с неверным паролем должен провалиться")
    void loginUser_shouldFail_withIncorrectPassword() {
        String login = "testUser";
        userService.registerUser(login, "correctPassword");

        Optional<User> result = userService.loginUser(login, "wrongPassword");

        Assertions.assertTrue(result.isEmpty(), "Optional должен быть пустым при неверном пароле");
    }

    @Test
    @DisplayName("Вход с несуществующим логином должен провалиться")
    void loginUser_shouldFail_withNonExistentLogin() {
        String login = "nonExistentUser";

        Optional<User> result = userService.loginUser(login, "anyPassword");

        Assertions.assertTrue(result.isEmpty(), "Optional должен быть пустым для несуществующего пользователя");
    }
}