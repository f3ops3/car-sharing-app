package app.carsharing.controller;

import app.carsharing.dto.user.UserDto;
import app.carsharing.dto.user.UserUpdateRequestDto;
import app.carsharing.dto.user.UserUpdateRoleRequestDto;
import app.carsharing.model.User;
import app.carsharing.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/role")
    public UserDto updateRole(@PathVariable Long id,
                              @RequestBody @Valid UserUpdateRoleRequestDto updateRoleRequestDto) {
        return userService.updateUserRole(id, updateRoleRequestDto);
    }

    @PutMapping("/me")
    public UserDto updateUserProfile(@AuthenticationPrincipal User user,
                                     @RequestBody @Valid UserUpdateRequestDto updateRequestDto) {
        return userService.updateUserProfile(user.getId(), updateRequestDto);
    }

    @GetMapping("/me")
    public UserDto getCurrentUser(@AuthenticationPrincipal User user) {
        return userService.getCurrentUserProfile(user);
    }
}
