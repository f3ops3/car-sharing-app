package app.carsharing.service;

import static app.carsharing.util.TestUtil.getUser;
import static app.carsharing.util.TestUtil.getUserResponseDto;
import static app.carsharing.util.TestUtil.userRegistrationRequestDto;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import app.carsharing.dto.user.UserDetailedDto;
import app.carsharing.dto.user.UserRegistrationRequestDto;
import app.carsharing.dto.user.UserResponseDto;
import app.carsharing.dto.user.UserUpdateRoleRequestDto;
import app.carsharing.dto.user.UserUpdateTgChatId;
import app.carsharing.exception.RegistrationException;
import app.carsharing.mapper.UserMapper;
import app.carsharing.model.User;
import app.carsharing.model.enums.Role;
import app.carsharing.repository.user.UserRepository;
import app.carsharing.service.user.impl.UserServiceImpl;
import app.carsharing.util.TestUtil;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void registerUser_ShouldRegisterUser() throws RegistrationException {
        // GIVE
        UserRegistrationRequestDto requestDto = userRegistrationRequestDto();
        User user = getUser(requestDto);
        UserResponseDto responseDto = getUserResponseDto(user);
        when(userRepository.existsByEmail(requestDto.getEmail())).thenReturn(false);
        when(userMapper.toEntity(requestDto)).thenReturn(user);
        when(passwordEncoder.encode(requestDto.getPassword())).thenReturn(requestDto.getPassword());
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(responseDto);

        // WHEN
        UserResponseDto actual = userService.register(requestDto);

        // THEN
        Assertions.assertNotNull(actual);
        verify(userRepository).save(user);
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(responseDto, actual, "id"));
    }

    @Test
    void registerUser_ShouldThrowException() {
        // GIVE
        UserRegistrationRequestDto requestDto = userRegistrationRequestDto();
        when(userRepository.existsByEmail(requestDto.getEmail())).thenReturn(true);

        // WHEN
        RegistrationException exception = assertThrows(RegistrationException.class,
                () -> userService.register(requestDto));

        // THEN
        assertNotNull(exception);
        assertEquals("User with email: "
                        + requestDto.getEmail() + " already exists",
                exception.getMessage());
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void updateUserRole_shouldUpdateUserRole() {
        // GIVE
        Long userId = 1L;
        User user = getUser(Role.CUSTOMER);
        UserUpdateRoleRequestDto requestDto = new UserUpdateRoleRequestDto();
        requestDto.setRole("MANAGER");
        UserDetailedDto responseDto = new UserDetailedDto();
        responseDto.setRole(user.getRole())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setEmail(user.getEmail());

        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toFullDto(user)).thenReturn(responseDto);

        // WHEN
        UserDetailedDto actual = userService.updateUserRole(userId, requestDto);

        // THEN
        assertNotNull(actual);
        assertEquals(responseDto.getRole(), actual.getRole());
    }

    @Test
    void updateUserTgChatId_shouldUpdateTgChatId() {
        // GIVE
        Long userId = 1L;
        User user = TestUtil.getUser(Role.CUSTOMER);
        UserUpdateTgChatId requestDto = new UserUpdateTgChatId();
        requestDto.setTgChaId(123456789L);

        UserDetailedDto responseDto = new UserDetailedDto();
        responseDto.setTgChatId(requestDto.getTgChaId())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setEmail(user.getEmail());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toFullDto(user)).thenReturn(responseDto);

        // WHEN
        UserDetailedDto actual = userService.updateUserTgChatId(userId, requestDto);

        // THEN
        assertNotNull(actual);
        assertEquals(responseDto.getTgChatId(), actual.getTgChatId());
    }
}
