package mate.academy.bookingapp.service.booking;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.bookingapp.model.Booking;
import mate.academy.bookingapp.model.BookingStatus;
import mate.academy.bookingapp.repository.booking.BookingRepository;
import mate.academy.bookingapp.service.notification.AddressFormatter;
import mate.academy.bookingapp.service.notification.NotificationService;
import mate.academy.bookingapp.service.notification.messagetemplate.BookingNotificationTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class BookingExpirationCheckService {

    private final BookingRepository bookingRepository;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 0 0 * * *", zone = "Europe/Paris")
    @Transactional
    public void checkExpiredBookings() {
        LocalDate today = LocalDate.now();
        List<BookingStatus> excludedStatuses = List.of(
                BookingStatus.CANCELLED, BookingStatus.EXPIRED);

        List<Booking> expiredBookings = bookingRepository.findExpiredBookings(
                excludedStatuses, today);

        if (expiredBookings.isEmpty()) {
            notificationService.sendNotification("No expired bookings today!");
            return;
        }

        for (Booking booking : expiredBookings) {
            booking.setStatus(BookingStatus.EXPIRED);
            bookingRepository.save(booking);

            notificationService.sendNotification(
                    BookingNotificationTemplate.BOOKING_EXPIRED.format(
                            booking.getId(),
                            booking.getAccommodation().getName(),
                            booking.getAccommodation().getType(),
                            booking.getCheckInDate(),
                            booking.getCheckOutDate(),
                            AddressFormatter.format(booking.getAccommodation().getLocation()),
                            booking.getUser().getFullName()
                    )
            );
        }
    }
}
