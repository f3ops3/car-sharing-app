package app.carsharing.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserUpdateRequestDto {
    @Email
    @Length(max = 60)
    private String email;
    @NotBlank
    @Length(max = 60)
    private String firstName;
    @NotBlank
    @Length(max = 60)
    private String lastName;
}
