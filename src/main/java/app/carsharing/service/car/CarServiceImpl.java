package app.carsharing.service.car;

import app.carsharing.dto.car.CarDetailedResponseDto;
import app.carsharing.dto.car.CarResponseDto;
import app.carsharing.dto.car.CarUpdateRequestDto;
import app.carsharing.dto.car.CreateCarRequestDto;
import app.carsharing.mapper.CarMapper;
import app.carsharing.model.Car;
import app.carsharing.repository.car.CarRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final CarMapper carMapper;

    @Override
    public CarDetailedResponseDto addCar(CreateCarRequestDto createCarRequestDto) {
        Car carToSave = carMapper.toEntity(createCarRequestDto);
        return carMapper.toDetailedDto(carRepository.save(carToSave));
    }

    @Override
    public Page<CarResponseDto> getAll(Pageable pageable) {
        return carRepository.findAll(pageable)
                .map(carMapper::toDto);
    }

    @Override
    public CarDetailedResponseDto getCarById(Long id) {
        Car car = carRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Car with id: " + id + " not found")
        );
        return carMapper.toDetailedDto(car);
    }

    @Override
    public CarDetailedResponseDto updateCarInventory(Long id, CarUpdateRequestDto dto) {
        Car car = carRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Car with id: " + id + " not found")
        );
        carMapper.patchCar(car, dto);
        return carMapper.toDetailedDto(carRepository.save(car));
    }

    @Override
    public CarDetailedResponseDto updateCar(Long id, CreateCarRequestDto dto) {
        Car car = carRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Car with id: " + id + " not found")
        );
        carMapper.updateCar(car, dto);
        return carMapper.toDetailedDto(carRepository.save(car));
    }

    @Override
    public void deleteCar(Long id) {
        Car car = carRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Car with id: " + id + " not found")
        );
        carRepository.delete(car);
    }
}
