package app.carsharing.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserUpdateRoleRequestDto {
    @NotBlank
    private String role;
}
