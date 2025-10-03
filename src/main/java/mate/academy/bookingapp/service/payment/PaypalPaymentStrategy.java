package mate.academy.bookingapp.service.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaypalPaymentStrategy implements PaymentStrategy {
    @Override
    public String createPaymentSession(Long bookingId) {
        return null;
    }
}
