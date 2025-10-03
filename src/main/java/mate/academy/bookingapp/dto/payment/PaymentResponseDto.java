package mate.academy.bookingapp.dto.payment;

import java.math.BigDecimal;
import java.net.URL;
import lombok.Data;
import mate.academy.bookingapp.model.PaymentStatus;

@Data
public class PaymentResponseDto {
    private Long id;
    private PaymentStatus status;
    private Long bookingId;
    private URL sessionUrl;
    private String sessionId;
    private BigDecimal amountToPay;
}
