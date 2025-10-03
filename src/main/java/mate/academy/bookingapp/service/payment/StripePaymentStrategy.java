package mate.academy.bookingapp.service.payment;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import io.github.cdimascio.dotenv.Dotenv;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import mate.academy.bookingapp.exception.EntityNotFoundException;
import mate.academy.bookingapp.exception.PaymentProcessingException;
import mate.academy.bookingapp.model.Booking;
import mate.academy.bookingapp.model.Payment;
import mate.academy.bookingapp.model.PaymentStatus;
import mate.academy.bookingapp.repository.booking.BookingRepository;
import mate.academy.bookingapp.repository.payment.PaymentRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StripePaymentStrategy implements PaymentStrategy {
    private static final String USD_CURRENCY = "usd";
    private static final Integer HUNDRED = 24;
    private static final Long ONE = 1L;
    private final Dotenv dotenv = Dotenv.load();
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final String successUrl = dotenv.get("PAYMENT_SUCCESS");
    private final String cancelUrl = dotenv.get("PAYMENT_CANCELED");

    public String createPaymentSession(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with booking id: "
                        + bookingId));

        long numberOfNights = ChronoUnit.DAYS.between(booking.getCheckInDate(),
                booking.getCheckOutDate());
        BigDecimal totalPriceInCents = booking.getAccommodation().getDailyRate()
                .multiply(BigDecimal.valueOf(numberOfNights))
                .multiply(BigDecimal.valueOf(HUNDRED));

        Stripe.apiKey = dotenv.get("STRIPE_SECRET_KEY");

        try {
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(successUrl + "?session_id={CHECKOUT_SESSION_ID}")
                    .setCancelUrl(cancelUrl + "?session_id={CHECKOUT_SESSION_ID}")
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(ONE)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency(USD_CURRENCY)
                                                    .setUnitAmountDecimal(totalPriceInCents)
                                                    .setProductData(
                                                            SessionCreateParams
                                                                    .LineItem
                                                                    .PriceData
                                                                    .ProductData
                                                                    .builder()
                                                                    .setName("Booking #"
                                                                            + bookingId)
                                                                    .build())
                                                    .build())
                                    .build())
                    .build();

            Session session = Session.create(params);

            Payment payment = new Payment();
            payment.setBooking(booking);
            payment.setStatus(PaymentStatus.PENDING);
            payment.setAmountToPay(totalPriceInCents);
            payment.setSessionId(session.getId());
            payment.setSessionUrl(new URL(session.getUrl()));
            paymentRepository.save(payment);

            return session.getUrl();
        } catch (StripeException | MalformedURLException e) {
            throw new PaymentProcessingException("Failed to create Stripe payment session", e);
        }
    }
}
