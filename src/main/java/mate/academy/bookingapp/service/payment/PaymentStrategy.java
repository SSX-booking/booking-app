package mate.academy.bookingapp.service.payment;

public interface PaymentStrategy {
    String createPaymentSession(Long bookingId);
}
