package app.carsharing.service;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import app.carsharing.dto.rental.CreateRentalRequestDto;
import app.carsharing.dto.rental.RentalActualReturnDateDto;
import app.carsharing.dto.rental.RentalDetailedDto;
import app.carsharing.dto.rental.RentalResponseDto;
import app.carsharing.dto.user.UserResponseDto;
import app.carsharing.exception.RentalException;
import app.carsharing.mapper.RentalMapper;
import app.carsharing.model.Car;
import app.carsharing.model.Rental;
import app.carsharing.model.User;
import app.carsharing.repository.car.CarRepository;
import app.carsharing.repository.rental.RentalRepository;
import app.carsharing.service.notification.impl.TelegramNotificationService;
import app.carsharing.service.rental.impl.RentalServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RentalServiceTest {
    private static final User USER = new User();
    @Mock
    private RentalMapper rentalMapper;
    @Mock
    private CarRepository carRepository;
    @Mock
    private RentalRepository rentalRepository;
    @Mock
    private TelegramNotificationService notificationService;
    @InjectMocks
    private RentalServiceImpl rentalService;

    @BeforeAll
    public static void setup() {
        USER.setId(1L);
    }

    @Test
    void createRental_ShouldCreateRental_WhenCarIsAvailable() {
        // GIVE
        CreateRentalRequestDto requestDto = new CreateRentalRequestDto();
        requestDto.setCarId(1L);
        Car car = new Car();
        car.setInventory(1);
        RentalDetailedDto responseDto = new RentalDetailedDto();
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(USER.getId());
        responseDto.setUser(userResponseDto);
        Rental rental = new Rental();

        when(carRepository.findById(requestDto.getCarId())).thenReturn(Optional.of(car));
        when(rentalMapper.toEntity(requestDto)).thenReturn(rental);
        when(rentalRepository.save(rental)).thenReturn(rental);
        when(rentalMapper.toDetailedDto(rental)).thenReturn(responseDto);

        // WHEN
        RentalDetailedDto result = rentalService.addRental(USER, requestDto);

        // THEN
        Assertions.assertNotNull(result);
        Assertions.assertEquals(USER.getId(), result.getUser().getId());
        verify(carRepository).save(car);
        verify(notificationService, never()).sendNotification(any(), any());
    }

    @Test
    void createRental_ShouldThrowException_WhenCarIsUnavailable() {
        // GIVEN
        CreateRentalRequestDto requestDto = new CreateRentalRequestDto();
        requestDto.setCarId(1L);
        Car car = new Car();
        car.setInventory(0);

        when(carRepository.findById(requestDto.getCarId())).thenReturn(Optional.of(car));

        // WHEN
        RentalException exception = assertThrows(RentalException.class,
                () -> rentalService.addRental(USER, requestDto));

        // THEN
        Assertions.assertNotNull(exception);
        Assertions.assertEquals("There is no available car", exception.getMessage());
        verify(carRepository).findById(requestDto.getCarId());
        verify(carRepository, times(0)).save(any(Car.class));
        verify(rentalRepository, times(0)).save(any(Rental.class));
    }

    @Test
    void findRentalById_ShouldReturnRental_WhenRentalExists() {
        // GIVEN
        Long rentalId = 1L;
        Rental rental = new Rental();
        RentalDetailedDto responseDto = new RentalDetailedDto();

        when(rentalRepository.findByIdAndUser(rentalId, USER))
                .thenReturn(Optional.of(rental));
        when(rentalMapper.toDetailedDto(rental)).thenReturn(responseDto);

        // WHEN
        RentalDetailedDto result = rentalService.getRentalById(rentalId, USER);

        // THEN
        verify(rentalRepository).findByIdAndUser(rentalId, USER);
        verify(rentalMapper).toDetailedDto(rental);
        Assertions.assertNotNull(result);
    }

    @Test
    void findRentalById_ShouldThrowException_WhenRentalDoesNotExist() {
        // GIVEN
        Long rentalId = 1L;

        // WHEN
        when(rentalRepository.findByIdAndUser(rentalId, USER)).thenReturn(Optional.empty());

        // THEN
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> rentalService.getRentalById(rentalId, USER));
        Assertions.assertEquals("Rental not found with id: " + rentalId + " and user with id: "
                + USER.getId(), exception.getMessage());
        verify(rentalRepository).findByIdAndUser(rentalId, USER);
    }

    @Test
    void returnRental_ShouldUpdateRental_WhenRentalExists() {
        // GIVEN
        Rental rental = new Rental();
        Car car = new Car();
        car.setId(1L);
        rental.setCar(car);
        RentalActualReturnDateDto requestDto = new RentalActualReturnDateDto();
        LocalDate actualReturnDate = LocalDate.of(2025, 1, 11);
        requestDto.setActualReturnDate(actualReturnDate);
        RentalResponseDto responseDto = new RentalResponseDto();
        responseDto.setActualReturnDate(actualReturnDate);
        Long rentalId = 1L;

        when(rentalRepository.findByIdAndUser(rentalId, USER))
                .thenReturn(Optional.of(rental));
        when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));
        doAnswer(invocation -> {
            Rental argRental = invocation.getArgument(0);
            argRental.setActualReturnDate(actualReturnDate);
            return null;
        }).when(rentalMapper).setActualReturnDate(any(Rental.class),
                any(RentalActualReturnDateDto.class));
        when(rentalRepository.save(rental)).thenReturn(rental);
        when(rentalMapper.toDto(rental)).thenReturn(responseDto);

        // WHEN
        RentalResponseDto result = rentalService.returnRental(USER, requestDto, rentalId);

        // THEN
        Assertions.assertNotNull(result);
        verify(rentalRepository).findByIdAndUser(rentalId, USER);
        verify(carRepository).findById(car.getId());
        verify(rentalRepository).save(rental);
        verify(rentalMapper).toDto(rental);
        Assertions.assertEquals(requestDto.getActualReturnDate(), result.getActualReturnDate());
    }
}
