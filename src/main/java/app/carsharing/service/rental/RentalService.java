package app.carsharing.service.rental;

import app.carsharing.dto.rental.CreateRentalRequestDto;
import app.carsharing.dto.rental.RentalActualReturnDateDto;
import app.carsharing.dto.rental.RentalDetailedDto;
import app.carsharing.dto.rental.RentalResponseDto;
import app.carsharing.dto.rental.RentalSearchParameters;
import app.carsharing.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RentalService {
    RentalDetailedDto addRental(User user, CreateRentalRequestDto requestDto);

    Page<RentalResponseDto> getRentals(User user, RentalSearchParameters searchParameters,
                                       Pageable pageable);

    RentalDetailedDto getRentalById(Long id, User user);

    RentalResponseDto returnRental(User user, RentalActualReturnDateDto dto, Long rentalId);
}
