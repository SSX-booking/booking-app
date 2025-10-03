package mate.academy.bookingapp.repository.emailverification;

import java.time.LocalDateTime;
import java.util.Optional;
import mate.academy.bookingapp.model.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailVerificationTokenRepository extends
        JpaRepository<EmailVerificationToken, Long> {
    Optional<EmailVerificationToken> findByEmailAndToken(String email, String token);

    void deleteByEmail(String email);

    void deleteAllByExpiryDateBefore(LocalDateTime time);

    boolean existsByEmail(String email);
}

