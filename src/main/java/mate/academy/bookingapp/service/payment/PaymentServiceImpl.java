package mate.academy.bookingapp.service.payment;

import lombok.RequiredArgsConstructor;
import mate.academy.bookingapp.dto.payment.PaymentResponseDto;
import mate.academy.bookingapp.exception.EntityNotFoundException;
import mate.academy.bookingapp.mapper.PaymentMapper;
import mate.academy.bookingapp.model.Booking;
import mate.academy.bookingapp.model.BookingStatus;
import mate.academy.bookingapp.model.Payment;
import mate.academy.bookingapp.model.PaymentStatus;
import mate.academy.bookingapp.repository.booking.BookingRepository;
import mate.academy.bookingapp.repository.payment.PaymentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final PaymentFactory paymentFactory;
    private final BookingRepository bookingRepository;

    @Override
    public Page<PaymentResponseDto> getAllPayments(Long userId, Pageable pageable) {
        return paymentRepository.findByBookingUserId(userId, pageable)
                .map(paymentMapper::toPaymentResponseDto);
    }

    @Override
    public Page<PaymentResponseDto> getPaymentsForUser(Long userId, Pageable pageable) {
        return paymentRepository.findByBookingUserId(userId, pageable)
                .map(paymentMapper::toPaymentResponseDto);
    }

    @Override
    public String createPayment(Long bookingId, Long userId, PaymentProvider provider) {
        PaymentStrategy strategy = paymentFactory.getStrategy(provider);
        return strategy.createPaymentSession(bookingId);
    }

    @Override
    public void handlePaymentSuccess(String sessionId) {
        Payment payment = paymentRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Payment session not found with id: "
                        + sessionId));

        payment.setStatus(PaymentStatus.COMPLETED);
        Booking booking = bookingRepository.findById(payment.getBooking().getId())
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: "
                        + payment.getBooking().getId()));
        booking.setStatus(BookingStatus.PAID);
        bookingRepository.save(booking);
        paymentRepository.save(payment);
    }

    @Override
    public void handlePaymentCancel(String sessionId) {
        Payment payment = paymentRepository.findBySessionId(sessionId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Payment session not found with sessionId: "
                                + sessionId));

        payment.setStatus(PaymentStatus.PENDING);
        paymentRepository.save(payment);
    }

    @Override
    public Payment findById(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with id: "
                        + paymentId));
    }
}
