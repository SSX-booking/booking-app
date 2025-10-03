package mate.academy.bookingapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.bookingapp.dto.email.EmailVerificationRequestDto;
import mate.academy.bookingapp.dto.user.UserLoginRequestDto;
import mate.academy.bookingapp.dto.user.UserLoginResponseDto;
import mate.academy.bookingapp.dto.user.UserRegistrationRequestDto;
import mate.academy.bookingapp.dto.user.UserResponseDto;
import mate.academy.bookingapp.exception.RegistrationException;
import mate.academy.bookingapp.security.AuthenticationService;
import mate.academy.bookingapp.service.emailverification.EmailVerificationService;
import mate.academy.bookingapp.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Users management",
        description = "Endpoints for managing authentication and authorization")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final EmailVerificationService emailVerificationService;

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register new user",
            description = "Register new user and returns it")
    public UserResponseDto register(@Valid @RequestBody UserRegistrationRequestDto request)
            throws RegistrationException {
        return userService.register(request);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Login user",
            description = "Login for already existing user")
    public UserLoginResponseDto login(@Valid @RequestBody UserLoginRequestDto request) {
        return authenticationService.authenticate(request);
    }

    @PostMapping("/verify-email")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Verify email",
            description = "Verify email for current user")
    public String verifyEmail(@Valid @RequestBody EmailVerificationRequestDto dto) {
        return userService.verifyCode(dto.getEmail(), dto.getCode());
    }

    @PostMapping("/resend-code")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Resend email code",
            description = "Resend email verification code")
    public String resendCode(@RequestParam String email) {
        return emailVerificationService.resendVerificationCode(email);
    }
}
