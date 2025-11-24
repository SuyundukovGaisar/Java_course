package ru.marketplace.catalog.web;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Утилита для валидации объектов с помощью Hibernate Validator.
 */
public class ValidationUtils {

    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();

    /**
     * Проверяет объект и возвращает сообщение об ошибке, если валидация не прошла.
     * @param object объект для проверки (например, ProductDto).
     * @return строка с ошибками или null, если ошибок нет.
     */
    public static <T> String validate(T object) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        if (violations.isEmpty()) {
            return null;
        }

        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
    }
}