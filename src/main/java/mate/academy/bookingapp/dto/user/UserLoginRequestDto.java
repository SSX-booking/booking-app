package mate.academy.bookingapp.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginRequestDto {
    @NotBlank(message = "Email cannot be empty")
    @Email
    @Size(min = 6, max = 50)
    private String email;
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, max = 40)
    private String password;
}
