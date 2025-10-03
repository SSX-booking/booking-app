package mate.academy.bookingapp.service.payment;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.bookingapp.model.Payment;
import mate.academy.bookingapp.model.PaymentStatus;
import mate.academy.bookingapp.repository.payment.PaymentRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentExpirationScheduler {
    private static final Integer TWENTY_FOUR_HOURS = 24;
    private final PaymentRepository paymentRepository;

    @Scheduled(fixedRate = 60000) // 1 min
    public void checkExpiredSessions() {
        List<Payment> pendingPayments = paymentRepository.findByStatus(PaymentStatus.PENDING);

        for (Payment payment : pendingPayments) {
            if (payment.getCreatedAt()
                    .isBefore(LocalDateTime.now()
                    .minusHours(TWENTY_FOUR_HOURS))) {
                payment.setStatus(PaymentStatus.EXPIRED);
                paymentRepository.save(payment);
            }
        }
    }
}
