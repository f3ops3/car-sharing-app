package app.carsharing.service.user.impl;

import app.carsharing.dto.user.UserDetailedDto;
import app.carsharing.dto.user.UserRegistrationRequestDto;
import app.carsharing.dto.user.UserResponseDto;
import app.carsharing.dto.user.UserUpdateRequestDto;
import app.carsharing.dto.user.UserUpdateRoleRequestDto;
import app.carsharing.dto.user.UserUpdateTgChatId;
import app.carsharing.exception.DataProcessingException;
import app.carsharing.exception.RegistrationException;
import app.carsharing.mapper.UserMapper;
import app.carsharing.model.User;
import app.carsharing.model.enums.Role;
import app.carsharing.repository.user.UserRepository;
import app.carsharing.service.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto userRegistrationRequestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(userRegistrationRequestDto.getEmail())) {
            throw new RegistrationException("User with email: "
                    + userRegistrationRequestDto.getEmail() + " already exists");
        }
        User user = userMapper.toEntity(userRegistrationRequestDto);
        user.setRole(Role.CUSTOMER);
        user.setPassword(passwordEncoder.encode(userRegistrationRequestDto.getPassword()));
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public UserDetailedDto getCurrentUserProfile(User user) {
        return userMapper.toFullDto(user);
    }

    @Override
    public UserDetailedDto updateUserRole(Long id, UserUpdateRoleRequestDto updateRoleRequestDto) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User with id: " + id + " not found")
        );
        try {
            user.setRole(Role.valueOf(updateRoleRequestDto.getRole()));
        } catch (IllegalArgumentException e) {
            throw new DataProcessingException("Invalid role with name: "
                    + updateRoleRequestDto.getRole());
        }
        return userMapper.toFullDto(userRepository.save(user));
    }

    @Override
    public UserDetailedDto updateUserProfile(Long userId, UserUpdateRequestDto updateRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User with id: " + userId + " not found")
        );
        userMapper.updateUser(user, updateRequestDto);
        return userMapper.toFullDto(userRepository.save(user));
    }

    @Override
    public UserDetailedDto updateUserTgChatId(Long id, UserUpdateTgChatId userUpdateTgChatId) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User with id: " + id + " not found")
        );
        user.setTgChatId(userUpdateTgChatId.getTgChaId());
        return userMapper.toFullDto(userRepository.save(user));
    }
}
