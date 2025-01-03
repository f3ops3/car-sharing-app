package app.carsharing.mapper;

import app.carsharing.config.MapperConfig;
import app.carsharing.dto.user.UserDetailedDto;
import app.carsharing.dto.user.UserRegistrationRequestDto;
import app.carsharing.dto.user.UserResponseDto;
import app.carsharing.dto.user.UserUpdateRequestDto;
import app.carsharing.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    User toEntity(UserRegistrationRequestDto userRegistrationRequestDto);

    UserDetailedDto toFullDto(User user);

    void updateUser(@MappingTarget User user,
                    UserUpdateRequestDto userUpdateRequestDto);
}
