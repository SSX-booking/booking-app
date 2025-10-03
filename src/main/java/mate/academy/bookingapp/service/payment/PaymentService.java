package mate.academy.bookingapp.service.payment;

import mate.academy.bookingapp.dto.payment.PaymentResponseDto;
import mate.academy.bookingapp.model.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentService {

    Page<PaymentResponseDto> getAllPayments(Long userId, Pageable pageable);

    Page<PaymentResponseDto> getPaymentsForUser(Long userId, Pageable pageable);

    String createPayment(Long bookingId, Long userId, PaymentProvider provider);

    void handlePaymentSuccess(String sessionId);

    void handlePaymentCancel(String sessionId);

    Payment findById(Long paymentId);
}
