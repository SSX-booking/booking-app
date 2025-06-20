package mate.academy.bookingapp.repository.booking;

import mate.academy.bookingapp.model.Booking;
import mate.academy.bookingapp.model.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Page<Booking> findByUserIdAndStatus(Long userId, BookingStatus status, Pageable pageable);

    Page<Booking> findByUserId(Long userId, Pageable pageable);

    Page<Booking> findByStatus(BookingStatus status, Pageable pageable);
}
