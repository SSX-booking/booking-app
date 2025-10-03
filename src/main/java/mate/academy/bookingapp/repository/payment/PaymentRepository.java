package mate.academy.bookingapp.repository.payment;

import java.util.List;
import java.util.Optional;
import mate.academy.bookingapp.model.Payment;
import mate.academy.bookingapp.model.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Page<Payment> findByBookingUserId(Long userId, Pageable pageable);

    Optional<Payment> findBySessionId(String sessionId);

    List<Payment> findByStatus(PaymentStatus status);
}

