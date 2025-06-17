package mate.academy.bookingapp.mapper;

import mate.academy.bookingapp.config.MapperConfig;
import mate.academy.bookingapp.dto.address.AddressResponseDto;
import mate.academy.bookingapp.dto.address.CreateAddressRequestDto;
import mate.academy.bookingapp.model.Address;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface AddressMapper {
    AddressResponseDto toAddressResponseDto(Address address);

    Address toAddress(CreateAddressRequestDto requestDto);
}
