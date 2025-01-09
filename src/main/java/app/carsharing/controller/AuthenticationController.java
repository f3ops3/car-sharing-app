package app.carsharing.controller;

import app.carsharing.dto.user.UserLoginRequestDto;
import app.carsharing.dto.user.UserLoginResponseDto;
import app.carsharing.dto.user.UserRegistrationRequestDto;
import app.carsharing.dto.user.UserResponseDto;
import app.carsharing.exception.RegistrationException;
import app.carsharing.security.AuthenticationService;
import app.carsharing.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Books store", description = "Endpoints for authentication")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Operation(summary = "Register new user")
    @PostMapping("/registration")
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto request)
            throws RegistrationException {
        return userService.register(request);
    }

    @Operation(summary = "Login in")
    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto request) {
        return authenticationService.authenticate(request);
    }
}
