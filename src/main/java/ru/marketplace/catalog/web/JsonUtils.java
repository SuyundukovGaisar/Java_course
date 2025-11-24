package ru.marketplace.catalog.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Утилитарный класс для работы с JSON в сервлетах.
 * Использует библиотеку Jackson для сериализации и десериализации.
 */
public class JsonUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.registerModule(new JavaTimeModule());
    }

    /**
     * Читает JSON из тела запроса и превращает его в Java-объект.
     *
     * @param req   HTTP запрос.
     * @param clazz класс, в который нужно превратить JSON.
     * @param <T>   тип объекта.
     * @return объект нужного типа.
     * @throws IOException если не удалось прочитать JSON.
     */
    public static <T> T readJson(HttpServletRequest req, Class<T> clazz) throws IOException {
        return mapper.readValue(req.getReader(), clazz);
    }

    /**
     * Превращает Java-объект в JSON и пишет его в ответ клиенту.
     * Устанавливает Content-Type: application/json.
     *
     * @param resp HTTP ответ.
     * @param data объект для отправки.
     * @throws IOException если не удалось записать JSON.
     */
    public static void writeJson(HttpServletResponse resp, Object data) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        mapper.writeValue(resp.getWriter(), data);
    }
}