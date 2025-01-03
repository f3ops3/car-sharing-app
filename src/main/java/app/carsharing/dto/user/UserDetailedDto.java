package app.carsharing.dto.user;

import app.carsharing.model.enums.Role;
import lombok.Data;

@Data
public class UserDetailedDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private Long tgChatId;
}
