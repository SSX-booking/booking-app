package mate.academy.bookingapp.service.notification.listener;

import lombok.RequiredArgsConstructor;
import mate.academy.bookingapp.service.notification.AddressFormatter;
import mate.academy.bookingapp.service.notification.NotificationService;
import mate.academy.bookingapp.service.notification.event.AccommodationCreatedEvent;
import mate.academy.bookingapp.service.notification.event.AccommodationDeletedEvent;
import mate.academy.bookingapp.service.notification.messagetemplate.AccommodationNotificationTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class AccommodationNotificationListener {
    private final NotificationService notificationService;

    @EventListener
    public void onAccommodationCreated(AccommodationCreatedEvent event) {
        var dto = event.accommodation();
        String message = AccommodationNotificationTemplate.ACCOMMODATION_CREATED.format(
                dto.getName(),
                dto.getType(),
                AddressFormatter.format(dto.getLocation()),
                dto.getSize(),
                String.join(", ", dto.getAmenities()),
                dto.getDailyRate(),
                dto.getAvailability()
        );
        notificationService.sendNotification(message);
    }

    @EventListener
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onAccommodationDeleted(AccommodationDeletedEvent event) {
        String message = AccommodationNotificationTemplate.ACCOMMODATION_DELETED.format(
                event.id(),
                event.name(),
                event.location()
        );
        notificationService.sendNotification(message);
    }
}
