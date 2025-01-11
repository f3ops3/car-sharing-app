package app.carsharing.dto.user;

import app.carsharing.model.enums.Role;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserDetailedDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private Long tgChatId;
}
