package mate.academy.bookingapp.service.notification.event;

import mate.academy.bookingapp.dto.booking.BookingResponseDto;

public record BookingCreatedEvent(BookingResponseDto booking) {
}
