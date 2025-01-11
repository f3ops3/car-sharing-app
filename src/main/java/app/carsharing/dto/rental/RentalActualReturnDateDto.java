package app.carsharing.dto.rental;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RentalActualReturnDateDto {
    @NotNull
    private LocalDate actualReturnDate;
}
