package mate.academy.bookingapp.service.emailverification;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import mate.academy.bookingapp.repository.emailverification.EmailVerificationTokenRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TokenCleanupService {
    private final EmailVerificationTokenRepository tokenRepository;

    @Scheduled(fixedRate = 600_000)
    public void cleanupExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        tokenRepository.deleteAllByExpiryDateBefore(now);
    }
}

