package mate.academy.bookingapp.dto.booking;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;
import mate.academy.bookingapp.model.Address;
import mate.academy.bookingapp.model.BookingStatus;

@Data
public class BookingResponseDto {
    private Long bookingId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String accommodationType;
    private Address location;
    private String size;
    private BigDecimal dailyRate;
    private BookingStatus status;
    private String bookedBy;
}
