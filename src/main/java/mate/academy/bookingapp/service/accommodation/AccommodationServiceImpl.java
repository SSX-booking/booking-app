package mate.academy.bookingapp.service.accommodation;

import lombok.RequiredArgsConstructor;
import mate.academy.bookingapp.dto.accommodation.AccommodationResponseDto;
import mate.academy.bookingapp.dto.accommodation.CreateAccommodationRequestDto;
import mate.academy.bookingapp.exception.EntityAlreadyExistsException;
import mate.academy.bookingapp.exception.EntityNotFoundException;
import mate.academy.bookingapp.mapper.AccommodationMapper;
import mate.academy.bookingapp.model.Accommodation;
import mate.academy.bookingapp.repository.accommodation.AccommodationRepository;
import mate.academy.bookingapp.service.notification.AccommodationNotificationTemplate;
import mate.academy.bookingapp.service.notification.AddressFormatter;
import mate.academy.bookingapp.service.notification.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccommodationServiceImpl implements AccommodationService {

    private final AccommodationMapper accommodationMapper;
    private final AccommodationRepository accommodationRepository;
    private final NotificationService notificationService;

    @Override
    public AccommodationResponseDto save(CreateAccommodationRequestDto requestDto) {
        if (accommodationRepository.existsByName(requestDto.getName())) {
            throw new EntityAlreadyExistsException("Accommodation already exists with given name: "
                    + requestDto.getName());
        }
        Accommodation accommodation = accommodationMapper.toAccommodation(requestDto);
        accommodationRepository.save(accommodation);

        notificationService.sendNotification(
                AccommodationNotificationTemplate.ACCOMMODATION_CREATED.format(
                        accommodation.getName(),
                        accommodation.getType(),
                        AddressFormatter.format(accommodation.getLocation()),
                        accommodation.getSize(),
                        String.join(", ", accommodation.getAmenities()),
                        accommodation.getDailyRate(),
                        accommodation.getAvailability()
                )
        );
        return accommodationMapper.toAccommodationDto(accommodation);
    }

    @Override
    public Page<AccommodationResponseDto> findAll(Pageable pageable) {
        return accommodationRepository.findAll(pageable)
                .map(accommodationMapper::toAccommodationDto);
    }

    @Override
    public AccommodationResponseDto findById(Long id) {
        Accommodation accommodation = accommodationRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Accommodation not found with id: " + id));
        return accommodationMapper.toAccommodationDto(accommodation);
    }

    @Override
    public AccommodationResponseDto updateById(Long id, CreateAccommodationRequestDto request) {
        Accommodation accommodation = accommodationRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Accommodation not found with id: " + id));
        return accommodationMapper.toAccommodationDto(accommodationRepository.save(accommodation));
    }

    @Override
    public void deleteById(Long id) {
        accommodationRepository.findById(id).ifPresent(accommodation ->
                notificationService.sendNotification(
                AccommodationNotificationTemplate.ACCOMMODATION_DELETED.format(
                        id,
                        accommodation.getName(),
                        AddressFormatter.format(accommodation.getLocation())
                )
        ));
        accommodationRepository.deleteById(id);
    }
}
