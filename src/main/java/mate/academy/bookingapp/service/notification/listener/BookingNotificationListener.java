package mate.academy.bookingapp.service.notification.listener;

import lombok.RequiredArgsConstructor;
import mate.academy.bookingapp.dto.booking.BookingResponseDto;
import mate.academy.bookingapp.service.notification.AddressFormatter;
import mate.academy.bookingapp.service.notification.TelegramNotificationService;
import mate.academy.bookingapp.service.notification.event.BookingCancelledEvent;
import mate.academy.bookingapp.service.notification.event.BookingCreatedEvent;
import mate.academy.bookingapp.service.notification.messagetemplate.BookingNotificationTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingNotificationListener {
    private final TelegramNotificationService telegramNotificationService;

    @EventListener
    public void onBookingCreated(BookingCreatedEvent event) {
        BookingResponseDto dto = event.booking();
        String message = BookingNotificationTemplate.BOOKING_CREATED.format(
                dto.getAccommodationName(),
                dto.getAccommodationType(),
                dto.getCheckInDate(),
                dto.getCheckOutDate(),
                AddressFormatter.format(dto.getLocation()),
                dto.getSize(),
                dto.getDailyRate(),
                dto.getStatus(),
                dto.getBookedBy()
        );
        telegramNotificationService.sendNotification(message);
    }

    @EventListener
    public void onBookingCancelled(BookingCancelledEvent event) {
        BookingResponseDto dto = event.booking();
        String message = BookingNotificationTemplate.BOOKING_CANCELLED.format(
                dto.getBookingId(),
                dto.getAccommodationName(),
                dto.getAccommodationType(),
                dto.getCheckInDate(),
                dto.getCheckOutDate(),
                AddressFormatter.format(dto.getLocation()),
                dto.getBookedBy()
        );
        telegramNotificationService.sendNotification(message);
    }
}
