package mate.academy.bookingapp.repository.role;

import java.util.Optional;
import mate.academy.bookingapp.model.Role;
import mate.academy.bookingapp.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
