package mate.academy.bookingapp.repository.accommodation;

import mate.academy.bookingapp.model.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {

    boolean existsByName(String name);
}
