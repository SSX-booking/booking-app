package mate.academy.bookingapp.service.booking;

import mate.academy.bookingapp.dto.booking.BookingResponseDto;
import mate.academy.bookingapp.dto.booking.CreateBookingRequestDto;
import mate.academy.bookingapp.model.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookingService {
    BookingResponseDto save(CreateBookingRequestDto booking, Long userId);

    Page<BookingResponseDto> findByUserIdAndStatus(
            Long userId,
            BookingStatus status,
            Pageable pageable);

    Page<BookingResponseDto> findUserBookings(Long userId, Pageable pageable);

    BookingResponseDto findById(Long bookingId);

    BookingResponseDto updateBooking(
            Long bookingId,
            CreateBookingRequestDto updatedBooking,
            Long userId);

    void deleteBooking(Long bookingId, Long userId);
}
