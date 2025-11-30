package ru.marketplace.catalog.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.marketplace.catalog.dto.UserDto;
import ru.marketplace.catalog.model.User;

/**
 * Маппер для преобразования сущности User в DTO и обратно.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserDto toDto(User user);
    User toEntity(UserDto dto);
}