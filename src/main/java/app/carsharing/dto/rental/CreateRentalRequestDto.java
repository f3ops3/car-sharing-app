package app.carsharing.dto.rental;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import lombok.Data;

@Data
public class CreateRentalRequestDto {
    @NotNull
    private LocalDate rentalDate;
    @NotNull
    private LocalDate returnDate;
    @NotNull
    @Positive
    private Long carId;
}
