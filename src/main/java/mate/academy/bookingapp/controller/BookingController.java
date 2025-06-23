package mate.academy.bookingapp.controller;

import lombok.RequiredArgsConstructor;
import mate.academy.bookingapp.dto.booking.BookingResponseDto;
import mate.academy.bookingapp.dto.booking.CreateBookingRequestDto;
import mate.academy.bookingapp.model.BookingStatus;
import mate.academy.bookingapp.model.User;
import mate.academy.bookingapp.service.booking.BookingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public BookingResponseDto createBooking(@RequestBody CreateBookingRequestDto booking,
                                            Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        return bookingService.save(booking, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<BookingResponseDto> getBookingsByUserIdAndStatus(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) BookingStatus status,
            Pageable pageable) {
        return bookingService.findByUserIdAndStatus(userId, status, pageable);
    }

    @GetMapping("/my")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public Page<BookingResponseDto> getUserBookings(Pageable pageable,
                                                    Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        return bookingService.findUserBookings(userId, pageable);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public BookingResponseDto getBookingById(@PathVariable Long id, Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        return bookingService.findById(id, userId);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public BookingResponseDto updateBookingDetailsById(
            @PathVariable Long id,
            @RequestBody CreateBookingRequestDto updatedBooking,
            Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        return bookingService.updateBooking(id, updatedBooking, userId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public void deleteBookingById(@PathVariable Long id,
                                  Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        bookingService.deleteBooking(id, userId);
    }

    private Long getCurrentUserId(Authentication authentication) {
        return ((User) authentication.getPrincipal()).getId();
    }
}


