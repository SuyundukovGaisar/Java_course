package ru.marketplace.catalog.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.marketplace.catalog.dto.UserDto;
import ru.marketplace.catalog.model.User;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toDto(User user);
    User toEntity(UserDto dto);
}