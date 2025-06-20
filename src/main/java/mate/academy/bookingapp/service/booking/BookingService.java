package mate.academy.bookingapp.service.booking;

import mate.academy.bookingapp.dto.booking.BookingResponseDto;
import mate.academy.bookingapp.dto.booking.CreateBookingRequestDto;
import mate.academy.bookingapp.model.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookingService {
    public BookingResponseDto save(CreateBookingRequestDto booking, Long userId);

    public Page<BookingResponseDto> findByUserIdAndStatus(
            Long userId,
            BookingStatus status,
            Pageable pageable);

    public Page<BookingResponseDto> findUserBookings(Long userId, Pageable pageable);

    public BookingResponseDto findById(Long bookingId);

    public BookingResponseDto updateBooking(
            Long bookingId,
            CreateBookingRequestDto updatedBooking,
            Long userId);

    void deleteBooking(Long bookingId, Long userId);
}
