package mate.academy.bookingapp.service.emailverification;

import java.time.LocalDateTime;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import mate.academy.bookingapp.exception.EntityAlreadyExistsException;
import mate.academy.bookingapp.exception.EntityNotFoundException;
import mate.academy.bookingapp.model.EmailVerificationToken;
import mate.academy.bookingapp.repository.emailverification.EmailVerificationTokenRepository;
import mate.academy.bookingapp.repository.user.UserRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {
    private static final Integer MAX_TOKEN_VALUE = 999999;
    private static final Integer TOKEN_ALIVE_MINUTES_TIME = 5;
    private final EmailVerificationTokenRepository tokenRepository;
    private final JavaMailSender mailSender;
    private final UserRepository userRepository;

    public void sendVerificationCode(String email) {
        if (tokenRepository.existsByEmail(email)) {
            throw new EntityAlreadyExistsException("Token already exists for email: " + email);
        }

        String code = String.format("%06d", new Random().nextInt(MAX_TOKEN_VALUE));

        EmailVerificationToken token = new EmailVerificationToken();
        token.setEmail(email);
        token.setToken(code);
        token.setExpiryDate(LocalDateTime.now().plusMinutes(TOKEN_ALIVE_MINUTES_TIME));
        tokenRepository.save(token);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Email Verification Code");
        message.setText("Your verification code is: "
                + code
                + "\nIt is valid for 5 minutes.");
        mailSender.send(message);
    }

    public String resendVerificationCode(String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new EntityNotFoundException("User not found for email: " + email);
        }
        tokenRepository.deleteByEmail(email);
        sendVerificationCode(email);

        return "A new verification code has been sent to your email. You can ignore previous code.";
    }

    public boolean verifyCode(String email, String code) {
        return tokenRepository.findByEmailAndToken(email, code)
                .filter(token -> !token.isExpired())
                .map(token -> {
                    tokenRepository.delete(token);
                    return true;
                })
                .orElse(false);
    }
}
