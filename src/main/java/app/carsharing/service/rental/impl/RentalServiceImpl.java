package app.carsharing.service.rental.impl;

import app.carsharing.dto.rental.CreateRentalRequestDto;
import app.carsharing.dto.rental.RentalActualReturnDateDto;
import app.carsharing.dto.rental.RentalDetailedDto;
import app.carsharing.dto.rental.RentalResponseDto;
import app.carsharing.dto.rental.RentalSearchParameters;
import app.carsharing.exception.RentalException;
import app.carsharing.mapper.RentalMapper;
import app.carsharing.model.Car;
import app.carsharing.model.Rental;
import app.carsharing.model.User;
import app.carsharing.repository.SpecificationBuilder;
import app.carsharing.repository.car.CarRepository;
import app.carsharing.repository.rental.RentalRepository;
import app.carsharing.service.rental.RentalService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {
    private final RentalRepository rentalRepository;
    private final CarRepository carRepository;
    private final RentalMapper rentalMapper;
    private final SpecificationBuilder<Rental> specificationBuilder;

    @Transactional
    @Override
    public RentalDetailedDto addRental(User user, CreateRentalRequestDto requestDto) {
        Car car = carRepository.findById(requestDto.getCarId()).orElseThrow(
                () -> new EntityNotFoundException("Car not found with id " + requestDto.getCarId())
        );
        if (car.getInventory() < 1) {
            throw new RentalException("There is no available car");
        }
        Rental rental = rentalMapper.toEntity(requestDto);
        rental.setUser(user);
        rental.setCar(car);
        car.setInventory(car.getInventory() - 1);
        carRepository.save(car);
        return rentalMapper.toDetailedDto(rentalRepository.save(rental));
    }

    @Override
    public Page<RentalResponseDto> getRentals(User user,
                                              RentalSearchParameters searchParameters,
                                              Pageable pageable) {
        boolean isAdmin = user.getAuthorities()
                .stream()
                .anyMatch(auth -> "ROLE_ADMIN".equals(auth.getAuthority()));
        if (!isAdmin) {
            if (searchParameters.getUserId() == null) {
                searchParameters.setUserId(user.getId().toString());
            } else if (!searchParameters.getUserId().equals(user.getId().toString())) {
                throw new AccessDeniedException("You do not have permission "
                        + "to access rentals of user with id " + searchParameters.getUserId());
            }
        }
        Specification<Rental> specification = specificationBuilder.build(searchParameters);
        return rentalRepository.findAll(specification, pageable).map(rentalMapper::toDto);
    }

    @Override
    public RentalDetailedDto getRentalById(Long id, User user) {
        Rental rental = rentalRepository.findByIdAndUser(id, user).orElseThrow(
                () -> new EntityNotFoundException("Rental not found with id: " + id
                        + " and user with id: " + user.getId())
        );
        return rentalMapper.toDetailedDto(rental);
    }

    @Transactional
    @Override
    public RentalResponseDto returnRental(User user, RentalActualReturnDateDto dto,
                                          Long rentalId) {
        Rental rental = rentalRepository.findByIdAndUser(rentalId, user).orElseThrow(
                () -> new EntityNotFoundException("Rental not found with id: " + rentalId
                        + " and user with id: " + user.getId())
        );
        if (rental.getActualReturnDate() != null) {
            throw new RentalException("Rental with id:" + rentalId + " already returned");
        }
        Long carId = rental.getCar().getId();
        Car car = carRepository.findById(carId).orElseThrow(
                () -> new EntityNotFoundException("Can't find car by id = " + carId));
        car.setInventory(car.getInventory() + 1);
        carRepository.save(car);
        rentalMapper.setActualReturnDate(rental, dto);
        return rentalMapper.toDto(rentalRepository.save(rental));
    }
}
