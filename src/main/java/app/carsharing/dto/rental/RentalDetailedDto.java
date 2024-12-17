package app.carsharing.dto.rental;

import app.carsharing.dto.car.CarDetailedResponseDto;
import app.carsharing.dto.user.UserResponseDto;
import java.time.LocalDate;
import lombok.Data;

@Data
public class RentalDetailedDto {
    private Long id;
    private LocalDate rentalDate;
    private LocalDate returnDate;
    private LocalDate actualReturnDate;
    private CarDetailedResponseDto carDetailed;
    private UserResponseDto user;
}
