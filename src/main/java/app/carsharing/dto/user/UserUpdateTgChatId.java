package app.carsharing.dto.user;

import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserUpdateTgChatId {
    @Positive
    private Long tgChaId;
}
