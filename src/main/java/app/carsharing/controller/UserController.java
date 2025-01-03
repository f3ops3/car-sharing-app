package app.carsharing.controller;

import app.carsharing.dto.user.UserDetailedDto;
import app.carsharing.dto.user.UserUpdateRequestDto;
import app.carsharing.dto.user.UserUpdateRoleRequestDto;
import app.carsharing.dto.user.UserUpdateTgChatId;
import app.carsharing.model.User;
import app.carsharing.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
    @PatchMapping("/{id}/role")
    public UserDetailedDto updateRole(@PathVariable Long id,
                                      @RequestBody @Valid UserUpdateRoleRequestDto updateRoleDto) {
        return userService.updateUserRole(id, updateRoleDto);
    }

    @PutMapping("/me")
    public UserDetailedDto updateUserProfile(@AuthenticationPrincipal User user,
                                             @RequestBody @Valid UserUpdateRequestDto updateDto) {
        return userService.updateUserProfile(user.getId(), updateDto);
    }

    @GetMapping("/me")
    public UserDetailedDto getCurrentUser(@AuthenticationPrincipal User user) {
        return userService.getCurrentUserProfile(user);
    }

    @PatchMapping("/telegram")
    public UserDetailedDto updateTgChatId(@AuthenticationPrincipal User user,
                                          @RequestBody @Valid UserUpdateTgChatId updateTgChatId) {
        return userService.updateUserTgChatId(user.getId(), updateTgChatId);
    }
}
