package mate.academy.bookingapp.dto.accommodation;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import mate.academy.bookingapp.dto.address.AddressResponseDto;
import mate.academy.bookingapp.model.AccommodationType;

@Data
public class AccommodationResponseDto {
    private Long id;
    private String name;
    private AccommodationType type;
    private AddressResponseDto location;
    private String size;
    private List<String> amenities;
    private BigDecimal dailyRate;
    private Integer availability;
}
