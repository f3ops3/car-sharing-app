package app.carsharing.mapper;

import app.carsharing.config.MapperConfig;
import app.carsharing.dto.rental.CreateRentalRequestDto;
import app.carsharing.dto.rental.RentalActualReturnDateDto;
import app.carsharing.dto.rental.RentalDetailedDto;
import app.carsharing.dto.rental.RentalResponseDto;
import app.carsharing.model.Rental;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface RentalMapper {
    Rental toEntity(CreateRentalRequestDto createRentalRequestDto);

    @Mapping(target = "carDetailed", source = "car")
    RentalDetailedDto toDetailedDto(Rental rental);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "carId", source = "car.id")
    RentalResponseDto toDto(Rental rental);

    void setActualReturnDate(@MappingTarget Rental rental,
                             RentalActualReturnDateDto rentalReturnDateRequestDto);
}
