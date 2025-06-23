package mate.academy.bookingapp.service.booking;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mate.academy.bookingapp.dto.booking.BookingResponseDto;
import mate.academy.bookingapp.dto.booking.CreateBookingRequestDto;
import mate.academy.bookingapp.exception.AccommodationNotAvailableException;
import mate.academy.bookingapp.exception.DuplicateBookingException;
import mate.academy.bookingapp.mapper.BookingMapper;
import mate.academy.bookingapp.model.Accommodation;
import mate.academy.bookingapp.model.Booking;
import mate.academy.bookingapp.model.BookingStatus;
import mate.academy.bookingapp.model.User;
import mate.academy.bookingapp.repository.accommodation.AccommodationRepository;
import mate.academy.bookingapp.repository.booking.BookingRepository;
import mate.academy.bookingapp.repository.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final AccommodationRepository accommodationRepository;
    private final BookingMapper bookingMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public BookingResponseDto save(CreateBookingRequestDto bookingDto, Long userId) {
        Accommodation accommodation =
                accommodationRepository.findById(bookingDto.getAccommodationId())
                .orElseThrow(() -> new RuntimeException("Accommodation not found with id: "
                        + bookingDto.getAccommodationId()));

        boolean alreadyBooked = bookingRepository
                .existsByAccommodationIdAndCheckInDateAndCheckOutDateAndUserId(
                        accommodation.getId(),
                        bookingDto.getCheckInDate(),
                        bookingDto.getCheckOutDate(),
                        userId);
        if (alreadyBooked) {
            throw new DuplicateBookingException(
                    "This accommodation is already booked on check in date:  "
                            + bookingDto.getCheckInDate() + ", check out date: "
                            + bookingDto.getCheckOutDate()
            );
        }

        if (accommodation.getAvailability() <= 0) {
            throw new AccommodationNotAvailableException("Accommodation is not available");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Booking booking = bookingMapper.toBooking(bookingDto);
        booking.setUser(user);
        booking.setStatus(BookingStatus.PENDING);
        booking.setAccommodation(accommodation);

        accommodation.setAvailability(accommodation.getAvailability() - 1);
        accommodationRepository.save(accommodation);

        Booking savedBooking = bookingRepository.save(booking);
        return bookingMapper.toBookingResponseDto(savedBooking);
    }

    @Override
    public Page<BookingResponseDto> findByUserIdAndStatus(Long userId,
                                                          BookingStatus status,
                                                          Pageable pageable) {
        Page<Booking> bookings;

        if (userId != null && status != null) {
            bookings = bookingRepository.findByUserIdAndStatus(userId, status, pageable);
        } else if (userId != null) {
            bookings = bookingRepository.findByUserId(userId, pageable);
        } else if (status != null) {
            bookings = bookingRepository.findByStatus(status, pageable);
        } else {
            bookings = bookingRepository.findAll(pageable);
        }
        return bookings.map(bookingMapper::toBookingResponseDto);
    }

    @Override
    public Page<BookingResponseDto> findUserBookings(Long userId, Pageable pageable) {
        return bookingRepository.findByUserId(userId, pageable)
                .map(bookingMapper::toBookingResponseDto);
    }

    @Override
    public BookingResponseDto findById(Long id, Long userId) {
        Booking booking = bookingRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
        return bookingMapper.toBookingResponseDto(booking);
    }

    @Override
    public BookingResponseDto updateBooking(Long bookingId,
                                            CreateBookingRequestDto updatedBooking,
                                            Long userId) {
        Booking booking = bookingRepository.findByIdAndUserId(bookingId, userId)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));

        booking.setCheckInDate(updatedBooking.getCheckInDate());
        booking.setCheckOutDate(updatedBooking.getCheckOutDate());

        return bookingMapper.toBookingResponseDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public void deleteBooking(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findByIdAndUserId(bookingId, userId)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));

        booking.setStatus(BookingStatus.CANCELLED);

        bookingRepository.save(booking);
        bookingRepository.delete(booking);

        Accommodation accommodation = booking.getAccommodation();
        if (accommodation != null) {
            accommodation.setAvailability(accommodation.getAvailability() + 1);
            accommodationRepository.save(accommodation);
        }
    }
}
