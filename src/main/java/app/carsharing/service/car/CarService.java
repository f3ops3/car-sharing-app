package app.carsharing.service.car;

import app.carsharing.dto.car.CarDetailedResponseDto;
import app.carsharing.dto.car.CarResponseDto;
import app.carsharing.dto.car.CarUpdateRequestDto;
import app.carsharing.dto.car.CreateCarRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CarService {
    CarDetailedResponseDto addCar(CreateCarRequestDto createCarRequestDto);

    Page<CarResponseDto> getAll(Pageable pageable);

    CarDetailedResponseDto getCarById(Long id);

    CarDetailedResponseDto updateCarInventory(Long id, CarUpdateRequestDto dto);

    CarDetailedResponseDto updateCar(Long id, CreateCarRequestDto dto);

    void deleteCar(Long id);
}
