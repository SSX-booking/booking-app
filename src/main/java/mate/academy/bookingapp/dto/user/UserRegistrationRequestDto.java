package mate.academy.bookingapp.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mate.academy.bookingapp.validator.ValidatePassword;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@ValidatePassword
public class UserRegistrationRequestDto {
    @NotBlank(message = "Email cannot be empty")
    @Email
    private String email;
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, max = 40)
    private String password;
    @NotBlank(message = "Repeat password cannot be empty")
    private String repeatPassword;
    @NotBlank(message = "First name cannot be empty")
    private String firstName;
    @NotBlank(message = "Last name cannot be empty")
    private String lastName;
    @NotBlank(message = "Shipping address cannot be empty")
    private String shippingAddress;
}
