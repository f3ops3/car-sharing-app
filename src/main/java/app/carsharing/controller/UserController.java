package app.carsharing.controller;

import app.carsharing.dto.user.UserDetailedDto;
import app.carsharing.dto.user.UserUpdateRequestDto;
import app.carsharing.dto.user.UserUpdateRoleRequestDto;
import app.carsharing.dto.user.UserUpdateTgChatId;
import app.carsharing.model.User;
import app.carsharing.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Car sharing", description = "Endpoints for user management")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "Update specific user's role")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/role")
    public UserDetailedDto updateRole(@PathVariable Long id,
                                      @RequestBody @Valid UserUpdateRoleRequestDto updateRoleDto) {
        return userService.updateUserRole(id, updateRoleDto);
    }

    @Operation(summary = "Update specific user's profile")
    @PutMapping("/me")
    public UserDetailedDto updateUserProfile(@AuthenticationPrincipal User user,
                                             @RequestBody @Valid UserUpdateRequestDto updateDto) {
        return userService.updateUserProfile(user.getId(), updateDto);
    }

    @Operation(summary = "Get current user's profile")
    @GetMapping("/me")
    public UserDetailedDto getCurrentUser(@AuthenticationPrincipal User user) {
        return userService.getCurrentUserProfile(user);
    }

    @Operation(summary = "Update specific user's telegram chat id")
    @PatchMapping("/telegram")
    public UserDetailedDto updateTgChatId(@AuthenticationPrincipal User user,
                                          @RequestBody @Valid UserUpdateTgChatId updateTgChatId) {
        return userService.updateUserTgChatId(user.getId(), updateTgChatId);
    }
}
