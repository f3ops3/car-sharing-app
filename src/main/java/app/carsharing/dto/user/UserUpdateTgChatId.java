package app.carsharing.dto.user;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UserUpdateTgChatId {
    @Positive
    private Long tgChaId;
}
