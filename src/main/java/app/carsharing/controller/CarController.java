package app.carsharing.controller;

import app.carsharing.dto.car.CarDetailedResponseDto;
import app.carsharing.dto.car.CarResponseDto;
import app.carsharing.dto.car.CarUpdateRequestDto;
import app.carsharing.dto.car.CreateCarRequestDto;
import app.carsharing.service.car.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Car sharing", description = "Endpoints for car management")
@RestController
@RequiredArgsConstructor
@RequestMapping("cars")
public class CarController {
    private final CarService carService;

    @Operation(summary = "Add new car")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarDetailedResponseDto addCar(@RequestBody @Valid CreateCarRequestDto dto) {
        return carService.addCar(dto);
    }

    @Operation(summary = "Get all cars")
    @GetMapping
    public Page<CarResponseDto> getCars(Pageable pageable) {
        return carService.getAll(pageable);
    }

    @Operation(summary = "Get specific car by id")
    @GetMapping("/{id}")
    public CarDetailedResponseDto getCarById(@PathVariable Long id) {
        return carService.getCarById(id);
    }

    @Operation(summary = "Update car amount by id")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public CarDetailedResponseDto updateCarInventory(@PathVariable Long id,
                                                @RequestBody @Valid CarUpdateRequestDto dto) {
        return carService.updateCarInventory(id, dto);
    }

    @Operation(summary = "Update car by id")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public CarDetailedResponseDto updateCar(@PathVariable Long id,
                                            @RequestBody @Valid CreateCarRequestDto dto) {
        return carService.updateCar(id, dto);
    }

    @Operation(summary = "Remove specific car")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
    }
}
