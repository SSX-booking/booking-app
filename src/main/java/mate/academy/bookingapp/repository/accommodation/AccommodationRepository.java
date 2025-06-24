package mate.academy.bookingapp.repository.accommodation;

import java.util.Optional;
import mate.academy.bookingapp.model.Accommodation;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {

    @EntityGraph(attributePaths = {"amenities", "location"})
    Optional<Accommodation> findById(Long id);

    boolean existsByName(String name);
}
