package mate.academy.bookingapp.security;

import lombok.RequiredArgsConstructor;
import mate.academy.bookingapp.exception.EntityNotFoundException;
import mate.academy.bookingapp.repository.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new EntityNotFoundException("Cannot find user with email: " + email));
    }
}
