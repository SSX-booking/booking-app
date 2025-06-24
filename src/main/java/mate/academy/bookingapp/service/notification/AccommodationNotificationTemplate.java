package mate.academy.bookingapp.service.notification;

import lombok.Getter;

@Getter
public enum AccommodationNotificationTemplate {

    ACCOMMODATION_CREATED("""
        🆕 *Accommodation Created*
        🏨 Name: %s
        🏷️ Type: %s
        📍 Address: %s
        📐 Size: %s
        🛋️ Amenities: %s
        💲 Daily Rate: %.2f
        🛏️ Available: %d
        """),

    ACCOMMODATION_DELETED("""
        🗑️ *Accommodation Deleted*
        🆔 ID: %s
        🏨 Name: %s
        📍 Address: %s
        """);

    private final String template;

    AccommodationNotificationTemplate(String template) {
        this.template = template;
    }

    public String format(Object... args) {
        return String.format(this.template, args);
    }
}

