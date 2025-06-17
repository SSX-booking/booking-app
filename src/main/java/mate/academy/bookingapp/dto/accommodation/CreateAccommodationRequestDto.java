package mate.academy.bookingapp.dto.accommodation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mate.academy.bookingapp.dto.address.CreateAddressRequestDto;
import mate.academy.bookingapp.model.AccommodationType;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class CreateAccommodationRequestDto {
    @NotBlank
    private String name;

    @NotNull
    private AccommodationType type;

    @NotNull
    private CreateAddressRequestDto location;

    @NotBlank
    private String size;

    @NotNull
    private List<@NotBlank String> amenities;

    @NotNull
    @PositiveOrZero
    private BigDecimal dailyRate;

    @NotNull
    @PositiveOrZero
    private Integer availability;
}
