package mate.academy.bookingapp.service.user;

import mate.academy.bookingapp.dto.user.UpdateUserProfileRequestDto;
import mate.academy.bookingapp.dto.user.UpdateUserRoleRequestDto;
import mate.academy.bookingapp.dto.user.UserRegistrationRequestDto;
import mate.academy.bookingapp.dto.user.UserResponseDto;
import mate.academy.bookingapp.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto request) throws RegistrationException;

    UserResponseDto updateUserRole(Long id, UpdateUserRoleRequestDto request);

    UserResponseDto getCurrentUserProfile(Long userId);

    UserResponseDto updateCurrentUserProfile(Long userId, UpdateUserProfileRequestDto request);

    String verifyCode(String email, String code);
}
