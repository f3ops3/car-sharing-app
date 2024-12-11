package app.carsharing.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserUpdateRoleRequestDto {
    @NotBlank
    private String role;
}
