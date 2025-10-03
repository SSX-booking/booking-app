package mate.academy.bookingapp.service.notification.event;

import mate.academy.bookingapp.dto.accommodation.AccommodationResponseDto;

public record AccommodationCreatedEvent(AccommodationResponseDto accommodation) {
}
