package app.carsharing.service.user;

import app.carsharing.dto.user.UserDetailedDto;
import app.carsharing.dto.user.UserRegistrationRequestDto;
import app.carsharing.dto.user.UserResponseDto;
import app.carsharing.dto.user.UserUpdateRequestDto;
import app.carsharing.dto.user.UserUpdateRoleRequestDto;
import app.carsharing.dto.user.UserUpdateTgChatId;
import app.carsharing.exception.RegistrationException;
import app.carsharing.model.User;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto userRegistrationRequestDto)
            throws RegistrationException;

    UserDetailedDto getCurrentUserProfile(User user);

    UserDetailedDto updateUserRole(Long id, UserUpdateRoleRequestDto updateRoleRequestDto);

    UserDetailedDto updateUserProfile(Long userId, UserUpdateRequestDto updateRequestDto);

    UserDetailedDto updateUserTgChatId(Long id, UserUpdateTgChatId userUpdateTgChatId);
}
