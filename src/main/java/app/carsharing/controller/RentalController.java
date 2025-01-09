package app.carsharing.controller;

import app.carsharing.dto.rental.CreateRentalRequestDto;
import app.carsharing.dto.rental.RentalActualReturnDateDto;
import app.carsharing.dto.rental.RentalDetailedDto;
import app.carsharing.dto.rental.RentalResponseDto;
import app.carsharing.dto.rental.RentalSearchParameters;
import app.carsharing.model.User;
import app.carsharing.service.rental.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Car sharing", description = "Endpoints for rental management")
@RestController
@RequiredArgsConstructor
@RequestMapping("rentals")
public class RentalController {
    private final RentalService rentalService;

    @Operation(summary = "Create new rental")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RentalDetailedDto createRental(@AuthenticationPrincipal User user,
                                          @RequestBody @Valid CreateRentalRequestDto dto) {
        return rentalService.addRental(user, dto);
    }

    @Operation(summary = "Get all rentals")
    @GetMapping
    public Page<RentalResponseDto> getRentals(@AuthenticationPrincipal User user,
                                              @RequestBody RentalSearchParameters searchParameters,
                                              Pageable pageable) {
        return rentalService.getRentals(user, searchParameters, pageable);
    }

    @Operation(summary = "Get specific rental by id")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public RentalDetailedDto getRental(@AuthenticationPrincipal User user,
                                       @PathVariable Long id) {
        return rentalService.getRentalById(id, user);
    }

    @Operation(summary = "Return rental")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/{id}/return")
    public RentalResponseDto returnRental(@AuthenticationPrincipal User user,
                                          @RequestBody @Valid RentalActualReturnDateDto dto,
                                          @PathVariable Long id) {
        return rentalService.returnRental(user, dto, id);
    }
}
