package mate.academy.bookingapp.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRoleRequestDto {
    @NotNull(message = "Roles set cannot be empty or null")
    private Set<@NotBlank String> roles;
}
