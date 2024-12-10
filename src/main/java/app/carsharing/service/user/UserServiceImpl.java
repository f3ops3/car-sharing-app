package app.carsharing.service.user;

import app.carsharing.dto.user.UserRegistrationRequestDto;
import app.carsharing.dto.user.UserResponseDto;
import app.carsharing.exception.RegistrationException;
import app.carsharing.mapper.UserMapper;
import app.carsharing.model.User;
import app.carsharing.model.enums.Role;
import app.carsharing.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
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
}
