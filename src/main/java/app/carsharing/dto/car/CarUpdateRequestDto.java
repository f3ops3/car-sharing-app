package app.carsharing.dto.car;

import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CarUpdateRequestDto {
    @Positive
    private int inventory;
}
