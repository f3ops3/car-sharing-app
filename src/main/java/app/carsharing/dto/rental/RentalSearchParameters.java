package app.carsharing.dto.rental;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RentalSearchParameters {
    private String userId;
    @NotNull
    private String isActive;
}
