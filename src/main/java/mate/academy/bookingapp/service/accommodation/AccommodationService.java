package mate.academy.bookingapp.service.accommodation;

import mate.academy.bookingapp.dto.accommodation.AccommodationResponseDto;
import mate.academy.bookingapp.dto.accommodation.CreateAccommodationRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccommodationService {
    AccommodationResponseDto save(CreateAccommodationRequestDto requestDto);

    Page<AccommodationResponseDto> findAll(Pageable pageable);

    AccommodationResponseDto findById(Long id);

    AccommodationResponseDto updateById(Long id, CreateAccommodationRequestDto request);

    void deleteById(Long id);
}
