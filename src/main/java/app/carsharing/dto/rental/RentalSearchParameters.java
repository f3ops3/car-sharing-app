package app.carsharing.dto.rental;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RentalSearchParameters {
    private String userId;
    @NotNull
    private String isActive;
}
