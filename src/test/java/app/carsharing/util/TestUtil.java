package app.carsharing.util;

import app.carsharing.dto.car.CarDetailedResponseDto;
import app.carsharing.dto.car.CarResponseDto;
import app.carsharing.dto.car.CreateCarRequestDto;
import app.carsharing.dto.payment.PaymentDetailedResponseDto;
import app.carsharing.dto.rental.RentalDetailedDto;
import app.carsharing.dto.rental.RentalResponseDto;
import app.carsharing.dto.rental.RentalSearchParameters;
import app.carsharing.dto.user.UserResponseDto;
import app.carsharing.model.Car;
import app.carsharing.model.Payment;
import app.carsharing.model.Rental;
import app.carsharing.model.User;
import app.carsharing.model.enums.CarType;
import app.carsharing.model.enums.Role;
import app.carsharing.model.enums.Status;
import app.carsharing.model.enums.Type;
import java.math.BigDecimal;
import java.time.LocalDate;

public final class TestUtil {
    public static final String FIRST_CAR_MODEL = "Lanos";
    public static final String FIRST_CAR_BRAND = "Daewoo";
    public static final String FIRST_CAR_TYPE = "SEDAN";
    public static final int FIRST_CAR_INVENTORY = 15;
    public static final int FIRST_CAR_PRICE = 35;
    public static final String SECOND_CAR_MODEL = "GLE";
    public static final String SECOND_CAR_BRAND = "Mercedes";
    public static final String SECOND_CAR_TYPE = "SUV";
    public static final int SECOND_CAR_INVENTORY = 10;
    public static final int SECOND_CAR_PRICE = 100;
    public static final String UPDATE_CAR_MODEL = "Matiz";
    public static final String UPDATE_CAR_BRAND = "Daewoo";
    public static final String UPDATE_CAR_TYPE = "SEDAN";
    public static final int UPDATE_CAR_INVENTORY = 20;
    public static final int UPDATE_CAR_PRICE = 25;
    public static final String DEFAULT_USER_FIRSTNAME = "John";
    public static final String DEFAULT_USER_LASTNAME = "Snow";
    public static final String DEFAULT_USER_EMAIL = "johnsnow@example.com";
    public static final String DEFAULT_USER_PASSWORD = "12345678";
    public static final LocalDate DEFAULT_RENTAL_DATE = LocalDate.of(2025, 1, 10);
    public static final LocalDate DEFAULT_RETURN_DATE = LocalDate.of(2025, 1, 11);
    public static final LocalDate ACTIVE_RENTAL_DATE = LocalDate.of(2025, 1, 12);
    public static final LocalDate ACTIVE_RETURN_DATE = LocalDate.of(2025, 1, 19);

    private TestUtil() {
    }

    public static CarDetailedResponseDto getFirstCarDetailedResponseDto() {
        return new CarDetailedResponseDto()
                .setId(1L)
                .setModel(FIRST_CAR_MODEL)
                .setBrand(FIRST_CAR_BRAND)
                .setCarType(FIRST_CAR_TYPE)
                .setInventory(FIRST_CAR_INVENTORY)
                .setDailyFee(BigDecimal.valueOf(FIRST_CAR_PRICE).setScale(2));
    }

    public static CarDetailedResponseDto getSecondCarDetailedResponseDto() {
        return new CarDetailedResponseDto()
                .setId(2L)
                .setModel(SECOND_CAR_MODEL)
                .setBrand(SECOND_CAR_BRAND)
                .setCarType(SECOND_CAR_TYPE)
                .setInventory(SECOND_CAR_INVENTORY)
                .setDailyFee(BigDecimal.valueOf(SECOND_CAR_PRICE).setScale(2));
    }

    public static CarResponseDto getFirstCarResponseDto() {
        return new CarResponseDto()
                .setId(1L)
                .setModel(FIRST_CAR_MODEL)
                .setCarType(FIRST_CAR_TYPE);
    }

    public static CarResponseDto getSecondCarResponseDto() {
        return new CarResponseDto()
                .setId(2L)
                .setModel(SECOND_CAR_MODEL)
                .setCarType(SECOND_CAR_TYPE);
    }

    public static Car getFirstCar() {
        return new Car()
                .setId(1L)
                .setModel(FIRST_CAR_MODEL)
                .setBrand(FIRST_CAR_BRAND)
                .setCarType(CarType.valueOf(FIRST_CAR_TYPE))
                .setInventory(FIRST_CAR_INVENTORY)
                .setDailyFee(BigDecimal.valueOf(FIRST_CAR_PRICE).setScale(2));
    }

    public static CreateCarRequestDto createFirstCarRequestDto() {
        CarDetailedResponseDto responseDto = getFirstCarDetailedResponseDto();
        return new CreateCarRequestDto()
                .setModel(responseDto.getModel())
                .setBrand(responseDto.getBrand())
                .setCarType(CarType.valueOf(responseDto.getCarType()))
                .setInventory(responseDto.getInventory())
                .setDailyFee(responseDto.getDailyFee());
    }

    public static CreateCarRequestDto updateCarRequestDto() {
        return new CreateCarRequestDto()
                .setModel(UPDATE_CAR_MODEL)
                .setBrand(UPDATE_CAR_BRAND)
                .setCarType(CarType.valueOf(UPDATE_CAR_TYPE))
                .setInventory(UPDATE_CAR_INVENTORY)
                .setDailyFee(BigDecimal.valueOf(UPDATE_CAR_PRICE).setScale(2));
    }

    public static CarDetailedResponseDto updatedCarResponseDto() {
        CreateCarRequestDto requestDto = updateCarRequestDto();
        return new CarDetailedResponseDto()
                .setModel(requestDto.getModel())
                .setBrand(requestDto.getBrand())
                .setCarType(requestDto.getCarType().toString())
                .setInventory(requestDto.getInventory())
                .setDailyFee(requestDto.getDailyFee());
    }

    public static User getUser(Role roleName) {
        return new User()
                .setId(1L)
                .setFirstName(DEFAULT_USER_FIRSTNAME)
                .setLastName(DEFAULT_USER_LASTNAME)
                .setEmail(DEFAULT_USER_EMAIL)
                .setPassword(DEFAULT_USER_PASSWORD)
                .setRole(roleName);
    }

    public static RentalSearchParameters getRentalSearchParameters() {
        return new RentalSearchParameters()
                .setIsActive("true")
                .setUserId("2");
    }

    public static Rental getRental(User user) {
        return new Rental()
                .setId(1L)
                .setUser(user)
                .setCar(TestUtil.getFirstCar())
                .setRentalDate(DEFAULT_RENTAL_DATE)
                .setReturnDate(DEFAULT_RETURN_DATE)
                .setDeleted(false);
    }

    public static Payment getPayment(Rental rental) {
        return new Payment()
                .setId(1L)
                .setType(Type.PAYMENT)
                .setStatus(Status.PENDING)
                .setRental(rental)
                .setAmountToPay(BigDecimal.valueOf(180))
                .setSessionId("sessionId")
                .setSession("sessionUrl");
    }

    public static PaymentDetailedResponseDto paymentDetailedResponseDto(Payment payment) {
        return new PaymentDetailedResponseDto()
                .setId(payment.getId())
                .setType(payment.getType())
                .setStatus(payment.getStatus())
                .setRentalId(payment.getRental().getId())
                .setSessionId(payment.getSessionId())
                .setSessionUrl(payment.getSession())
                .setAmountToPay(payment.getAmountToPay());
    }

    public static UserResponseDto userResponseDto(User user) {
        return new UserResponseDto()
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setEmail(user.getEmail());
    }

    public static Rental getActiveRental(User user, Car car) {
        return new Rental()
                .setId(1L)
                .setUser(user)
                .setCar(car)
                .setRentalDate(ACTIVE_RENTAL_DATE)
                .setReturnDate(ACTIVE_RETURN_DATE);
    }

    public static RentalDetailedDto rentalDetailedResponseDto(Rental rental,
                                                              CarDetailedResponseDto carDto,
                                                              UserResponseDto userDto) {
        RentalDetailedDto dto = new RentalDetailedDto()
                .setId(rental.getId())
                .setCarDetailed(carDto)
                .setUser(userDto)
                .setRentalDate(rental.getRentalDate())
                .setReturnDate(rental.getReturnDate());
        dto.getUser().setId(2L);
        return dto;
    }

    public static RentalResponseDto rentalResponseDto(Rental rental,
                                                      Car car,
                                                      User user) {
        return new RentalResponseDto()
                .setId(rental.getId())
                .setCarId(car.getId())
                .setUserId(user.getId())
                .setRentalDate(rental.getRentalDate())
                .setReturnDate(rental.getReturnDate());
    }
}
