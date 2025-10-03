package mate.academy.bookingapp.dto.booking;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;

@Data
public class CreateBookingRequestDto {

    @NotNull(message = "Accommodation id cannot be null")
    private Long accommodationId;

    @NotNull(message = "Check-in date cannot be null")
    private LocalDate checkInDate;

    @NotNull(message = "Check-out date is required")
    @Future(message = "Check-out date must be in the future")
    private LocalDate checkOutDate;
}
