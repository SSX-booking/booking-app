package mate.academy.bookingapp.dto.booking;

import java.time.LocalDate;
import lombok.Data;

@Data
public class CreateBookingRequestDto {
    private Long accommodationId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}
