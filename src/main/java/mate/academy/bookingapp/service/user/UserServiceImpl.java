package mate.academy.bookingapp.service.user;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.bookingapp.dto.user.UserRegistrationRequestDto;
import mate.academy.bookingapp.dto.user.UserResponseDto;
import mate.academy.bookingapp.exception.EntityNotFoundException;
import mate.academy.bookingapp.exception.RegistrationException;
import mate.academy.bookingapp.mapper.UserMapper;
import mate.academy.bookingapp.model.Role;
import mate.academy.bookingapp.model.RoleName;
import mate.academy.bookingapp.model.User;
import mate.academy.bookingapp.repository.role.RoleRepository;
import mate.academy.bookingapp.repository.user.UserRepository;
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

    @Override
    public UserResponseDto register(UserRegistrationRequestDto request)
            throws RegistrationException {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RegistrationException("User with same email already exists. Email: "
                    + request.getEmail());
        }

        User user = userMapper.toUser(request);
        Role defaultRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Given role is not present in a database: " + RoleName.ROLE_USER.name())
                );
        user.setRoles(Set.of(defaultRole));
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return userMapper.toUserResponseDto(user);
    }
}
