package mate.academy.bookingapp.repository.booking;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import mate.academy.bookingapp.model.Booking;
import mate.academy.bookingapp.model.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Page<Booking> findByUserIdAndStatus(Long userId, BookingStatus status, Pageable pageable);

    boolean existsByUserIdAndStatus(Long userId, BookingStatus status);

    Page<Booking> findByUserId(Long userId, Pageable pageable);

    Page<Booking> findByStatus(BookingStatus status, Pageable pageable);

    Optional<Booking> findByIdAndUserId(Long bookingId, Long userId);

    boolean existsByAccommodationIdAndCheckInDateAndCheckOutDateAndUserId(Long accommodationId,
                                                                          LocalDate checkInDate,
                                                                          LocalDate checkOutDate,
                                                                          Long userId);

    @Query("SELECT b FROM Booking b "
            + "WHERE b.status NOT IN (:excludedStatuses) "
            + "AND b.checkOutDate <= :today")
    List<Booking> findExpiredBookings(
            @Param("excludedStatuses") List<BookingStatus> excludedStatuses,
            @Param("today") LocalDate today);
}
