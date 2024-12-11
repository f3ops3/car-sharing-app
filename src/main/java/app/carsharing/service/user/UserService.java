package app.carsharing.service.user;

import app.carsharing.dto.user.UserDto;
import app.carsharing.dto.user.UserRegistrationRequestDto;
import app.carsharing.dto.user.UserResponseDto;
import app.carsharing.dto.user.UserUpdateRequestDto;
import app.carsharing.dto.user.UserUpdateRoleRequestDto;
import app.carsharing.exception.RegistrationException;
import app.carsharing.model.User;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto userRegistrationRequestDto)
            throws RegistrationException;

    UserDto getCurrentUserProfile(User user);

    UserDto updateUserRole(Long id, UserUpdateRoleRequestDto updateRoleRequestDto);

    UserDto updateUserProfile(Long userId, UserUpdateRequestDto updateRequestDto);
}
