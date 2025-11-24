package ru.marketplace.catalog.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.marketplace.catalog.dto.UserDto;
import ru.marketplace.catalog.mapper.UserMapper;
import ru.marketplace.catalog.model.User;
import ru.marketplace.catalog.service.UserService;

import java.io.IOException;

@WebServlet(name = "UserServlet", urlPatterns = "/users/*")
public class UserServlet extends HttpServlet {

    private final UserService userService;

    public UserServlet(UserService userService) {
        this.userService = userService;
    }

    /**
     * POST /users/register - регистрация нового пользователя.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        String pathInfo = req.getPathInfo();

        if ("/register".equals(pathInfo)) {
            try {
                UserDto dto = JsonUtils.readJson(req, UserDto.class);

                String validationError = ValidationUtils.validate(dto);
                if (validationError != null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, validationError);
                    return;
                }

                boolean created = userService.registerUser(dto.getLogin(), dto.getPassword());

                if (created) {
                    resp.setStatus(HttpServletResponse.SC_CREATED);
                    resp.getWriter().write("User registered successfully");
                } else {
                    resp.sendError(HttpServletResponse.SC_CONFLICT, "User with this login already exists");
                }
            } catch (Exception e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid data");
            }
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}