package mate.academy.bookingapp.mapper;

import mate.academy.bookingapp.config.MapperConfig;
import mate.academy.bookingapp.dto.accommodation.AccommodationResponseDto;
import mate.academy.bookingapp.dto.accommodation.CreateAccommodationRequestDto;
import mate.academy.bookingapp.model.Accommodation;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class, uses = AddressMapper.class)
public interface AccommodationMapper {

    AccommodationResponseDto toAccommodationDto(Accommodation accommodation);

    Accommodation toAccommodation(CreateAccommodationRequestDto createAccommodationRequestDto);
}
