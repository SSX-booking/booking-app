package mate.academy.bookingapp.service.notification.event;

public record AccommodationDeletedEvent(Long id, String name, String location) {
}
