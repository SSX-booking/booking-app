package mate.academy.bookingapp.service.notification.messagetemplate;

import lombok.Getter;

@Getter
public enum BookingNotificationTemplate {

    BOOKING_CREATED("""
        📌 *New Booking Created*
        🏨 Name: %s
        🏷️ Type: %s
        📅 Check-In: %s
        📅 Check-Out: %s
        📍 Address: %s
        📐 Size: %s
        💲 Daily Rate: %.2f
        🔖 Payment Status: %s
        👤 Booked By: %s
        """),

    BOOKING_CANCELLED("""
        ❌ *Booking Cancelled*
        🆔 ID: %s
        🏨 Name: %s
        🏷️ Type: %s
        📅 Check-In: %s
        📅 Check-Out: %s
        📍 Address: %s
        👤 Booked By: %s
        """),

    BOOKING_EXPIRED("""
        ❌ *Booking Cancelled*
        🆔 ID: %s
        🏨 Name: %s
        🏷️ Type: %s
        📅 Check-In: %s
        📅 Check-Out: %s
        📍 Address: %s
        🔖 Payment Status: %s
        """);

    private final String template;

    BookingNotificationTemplate(String template) {
        this.template = template;
    }

    public String format(Object... args) {
        return String.format(this.template, args);
    }
}

