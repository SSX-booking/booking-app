package mate.academy.bookingapp.mapper;

import mate.academy.bookingapp.config.MapperConfig;
import mate.academy.bookingapp.dto.payment.PaymentResponseDto;
import mate.academy.bookingapp.model.Payment;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface PaymentMapper {
    PaymentResponseDto toPaymentResponseDto(Payment payment);
}
