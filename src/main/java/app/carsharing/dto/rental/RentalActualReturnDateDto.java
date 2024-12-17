package app.carsharing.dto.rental;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;

@Data
public class RentalActualReturnDateDto {
    @NotNull
    private LocalDate actualReturnDate;
}
