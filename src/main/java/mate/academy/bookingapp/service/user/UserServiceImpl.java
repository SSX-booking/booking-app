package mate.academy.bookingapp.service.user;

import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.academy.bookingapp.dto.user.UpdateUserProfileRequestDto;
import mate.academy.bookingapp.dto.user.UpdateUserRoleRequestDto;
import mate.academy.bookingapp.dto.user.UserRegistrationRequestDto;
import mate.academy.bookingapp.dto.user.UserResponseDto;
import mate.academy.bookingapp.exception.EntityNotFoundException;
import mate.academy.bookingapp.exception.InvalidEmailVerificationException;
import mate.academy.bookingapp.exception.RegistrationException;
import mate.academy.bookingapp.mapper.UserMapper;
import mate.academy.bookingapp.model.Role;
import mate.academy.bookingapp.model.RoleName;
import mate.academy.bookingapp.model.User;
import mate.academy.bookingapp.repository.role.RoleRepository;
import mate.academy.bookingapp.repository.user.UserRepository;
import mate.academy.bookingapp.service.emailverification.EmailVerificationService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto request)
            throws RegistrationException {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RegistrationException("User with same email already exists. Email: "
                    + request.getEmail());
        }

        User user = userMapper.toUser(request);
        Role defaultRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RegistrationException(
                        "Given role is not present in a database: " + RoleName.ROLE_USER.name())
                );
        user.setRoles(Set.of(defaultRole));
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(false);

        userRepository.save(user);

        emailVerificationService.sendVerificationCode(user.getEmail());

        return userMapper.toUserResponseDto(user);
    }

    @Override
    public UserResponseDto updateUserRole(Long id, UpdateUserRoleRequestDto request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        Set<Role> roles = request.getRoles().stream()
                .map(roleName -> roleRepository.findByName(RoleName.valueOf(roleName))
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Role not found: " + roleName)))
                .collect(Collectors.toSet());
        user.setRoles(roles);
        return userMapper.toUserResponseDto(userRepository.save(user));
    }

    @Override
    public UserResponseDto getCurrentUserProfile(Long id) {
        return userMapper.toUserResponseDto(userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: "
                        + id)));
    }

    @Override
    public UserResponseDto updateCurrentUserProfile(Long userId,
                                                    UpdateUserProfileRequestDto updateDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: "
                        + userId));
        user.setFirstName(updateDto.getFirstName());
        user.setLastName(updateDto.getLastName());

        return userMapper.toUserResponseDto(userRepository.save(user));
    }

    @Override
    public String verifyCode(String email, String code) {
        boolean success = emailVerificationService.verifyCode(email, code);
        if (!success) {
            throw new InvalidEmailVerificationException("Invalid email verification code.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: "
                        + email));
        user.setEnabled(true);
        userRepository.save(user);

        return "Email verified successfully. You can now log in.";
    }
}
