package ru.marketplace.catalog.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.marketplace.catalog.dto.UserDto;
import ru.marketplace.catalog.model.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-07T16:16:46+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto toDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setLogin( user.getLogin() );
        userDto.setPassword( user.getPassword() );

        return userDto;
    }

    @Override
    public User toEntity(UserDto dto) {
        if ( dto == null ) {
            return null;
        }

        User user = new User();

        user.setLogin( dto.getLogin() );
        user.setPassword( dto.getPassword() );

        return user;
    }
}
