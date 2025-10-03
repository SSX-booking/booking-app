package mate.academy.bookingapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.bookingapp.dto.user.UpdateUserProfileRequestDto;
import mate.academy.bookingapp.dto.user.UpdateUserRoleRequestDto;
import mate.academy.bookingapp.dto.user.UserResponseDto;
import mate.academy.bookingapp.model.User;
import mate.academy.bookingapp.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users management",
        description = "Endpoints for getting and updating user's info")
public class UserController {

    private final UserService userService;

    @PutMapping("/{id}/role")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update user's Role",
            description = "Updates user's role by userId")
    public UserResponseDto updateUserRole(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRoleRequestDto request) {
        return userService.updateUserRole(id, request);
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Get current user",
            description = "Returns information about current user")
    public UserResponseDto getCurrentUser(Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        return userService.getCurrentUserProfile(userId);
    }

    @PatchMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Update user's profile",
            description = "Updates current user's profile")
    public UserResponseDto updateCurrentUser(
            @Valid @RequestBody UpdateUserProfileRequestDto request,
            Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        return userService.updateCurrentUserProfile(userId, request);
    }

    private Long getCurrentUserId(Authentication authentication) {
        return ((User) authentication.getPrincipal()).getId();
    }
}
