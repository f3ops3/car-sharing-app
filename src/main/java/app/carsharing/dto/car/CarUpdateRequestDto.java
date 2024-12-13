package app.carsharing.dto.car;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CarUpdateRequestDto {
    @NotNull
    @Positive
    private int inventory;
}
