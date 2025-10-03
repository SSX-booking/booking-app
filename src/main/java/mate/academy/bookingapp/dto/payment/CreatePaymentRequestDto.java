package mate.academy.bookingapp.dto.payment;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreatePaymentRequestDto {
    @NotNull(message = "Booking id cannot be null")
    private Long bookingId;
    @NotNull(message = "User id cannot be null")
    private Long userId;
}
