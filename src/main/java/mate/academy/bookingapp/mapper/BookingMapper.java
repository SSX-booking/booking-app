package mate.academy.bookingapp.mapper;

import mate.academy.bookingapp.config.MapperConfig;
import mate.academy.bookingapp.dto.booking.BookingResponseDto;
import mate.academy.bookingapp.dto.booking.CreateBookingRequestDto;
import mate.academy.bookingapp.model.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = AddressMapper.class)
public interface BookingMapper {

    Booking toBooking(CreateBookingRequestDto dto);

    @Mapping(source = "id", target = "bookingId")
    @Mapping(source = "accommodation.type", target = "accommodationType")
    @Mapping(source = "accommodation.location", target = "location")
    @Mapping(source = "accommodation.size", target = "size")
    @Mapping(source = "accommodation.dailyRate", target = "dailyRate")
    @Mapping(expression = "java(booking.getUser()"
            + ".getFirstName() + \" \" + booking.getUser().getLastName())",
            target = "bookedBy")
    BookingResponseDto toBookingResponseDto(Booking booking);
}
