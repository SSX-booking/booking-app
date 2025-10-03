package mate.academy.bookingapp.service.payment;

import lombok.RequiredArgsConstructor;
import mate.academy.bookingapp.exception.PaymentProcessingException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentFactory {

    private final StripePaymentStrategy stripePaymentStrategy;
    private final PaypalPaymentStrategy paypalPaymentStrategy;

    public PaymentStrategy getStrategy(PaymentProvider provider) {
        return switch (provider) {
            case STRIPE -> stripePaymentStrategy;
            case PAYPALL -> paypalPaymentStrategy;
            default -> throw new PaymentProcessingException("Unsupported payment provider: "
                    + provider);
        };
    }
}
