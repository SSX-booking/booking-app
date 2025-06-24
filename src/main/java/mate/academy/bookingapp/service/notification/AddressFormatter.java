package mate.academy.bookingapp.service.notification;

import mate.academy.bookingapp.dto.address.AddressResponseDto;
import mate.academy.bookingapp.model.Address;

public class AddressFormatter {
    public static String format(Address address) {
        if (address == null) {
            return "N/A";
        }
        return String.format("%s, %s, %s",
                address.getStreet(),
                address.getCity(),
                address.getCountry());
    }

    public static String format(AddressResponseDto dto) {
        if (dto == null) {
            return "N/A";
        }
        return String.format("%s, %s, %s",
                dto.getStreet(),
                dto.getCity(),
                dto.getCountry());
    }
}
