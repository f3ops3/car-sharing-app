package app.carsharing.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import app.carsharing.dto.car.CarDetailedResponseDto;
import app.carsharing.dto.car.CarResponseDto;
import app.carsharing.dto.car.CarUpdateRequestDto;
import app.carsharing.dto.car.CreateCarRequestDto;
import app.carsharing.mapper.CarMapper;
import app.carsharing.model.Car;
import app.carsharing.model.enums.CarType;
import app.carsharing.repository.car.CarRepository;
import app.carsharing.service.car.impl.CarServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
public class CarServiceTest {
    @Mock
    private CarRepository carRepository;

    @Mock
    private CarMapper carMapper;

    @InjectMocks
    private CarServiceImpl carService;

    @Test
    void createCar_ShouldCreateCar_WhenCarTypeIsValid() {
        // GIVEN
        CreateCarRequestDto requestDto = new CreateCarRequestDto();
        requestDto.setCarType(CarType.valueOf("SUV"));
        Car car = new Car();
        CarDetailedResponseDto responseDto = new CarDetailedResponseDto();

        when(carMapper.toEntity(requestDto)).thenReturn(car);
        when(carRepository.save(car)).thenReturn(car);
        when(carMapper.toDetailedDto(car)).thenReturn(responseDto);

        // WHEN
        CarDetailedResponseDto result = carService.addCar(requestDto);

        // THEN
        assertNotNull(result);
        verify(carRepository).save(car);
    }

    @Test
    void findAllCars_ShouldReturnPageOfCars() {
        // GIVEN
        PageRequest pageable = PageRequest.of(0, 10);
        Car car = new Car();

        when(carRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(car)));

        // WHEN
        Page<CarResponseDto> result = carService.getAll(pageable);

        // THEN
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void findCarById_ShouldReturnCar_WhenCarExists() {
        // GIVEN
        Long carId = 1L;
        Car car = new Car();
        CarDetailedResponseDto responseDto = new CarDetailedResponseDto();

        when(carRepository.findById(carId)).thenReturn(Optional.of(car));
        when(carMapper.toDetailedDto(car)).thenReturn(responseDto);

        // WHEN
        CarDetailedResponseDto result = carService.getCarById(carId);

        // THEN
        assertNotNull(result);
    }

    @Test
    void findCarById_ShouldThrowException_WhenCarDoesNotExist() {
        // GIVEN
        Long carId = 1L;

        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        // WHEN
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> carService.getCarById(carId));

        // THEN
        assertTrue(exception.getMessage().contains("Car with id: " + carId + " not found"));
    }

    @Test
    void deleteCar_ShouldDeleteCar_WhenCarExists() {
        // GIVEN
        Long carId = 1L;
        Car car = new Car();

        when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        // WHEN
        carService.deleteCar(carId);

        // THEN
        verify(carRepository).delete(car);
    }

    @Test
    void updateCarInventory_ShouldUpdateInventory_WhenCarExists() {
        // GIVEN
        Long carId = 1L;
        Car car = new Car();
        CarUpdateRequestDto requestDto = new CarUpdateRequestDto();
        requestDto.setInventory(10);
        CarDetailedResponseDto responseDto = new CarDetailedResponseDto();

        when(carRepository.findById(carId)).thenReturn(Optional.of(car));
        doAnswer(invocation -> {
            car.setInventory(requestDto.getInventory());
            return null;
        }).when(carMapper).patchCar(car, requestDto);
        when(carRepository.save(car)).thenReturn(car);
        when(carMapper.toDetailedDto(car)).thenReturn(responseDto);

        // WHEN
        CarDetailedResponseDto result = carService.updateCarInventory(carId, requestDto);

        // THEN
        assertNotNull(result);
        assertEquals(10, car.getInventory());
        verify(carMapper).patchCar(car, requestDto);
    }
}
