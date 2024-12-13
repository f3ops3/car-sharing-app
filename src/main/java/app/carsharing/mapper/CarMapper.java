package app.carsharing.mapper;

import app.carsharing.config.MapperConfig;
import app.carsharing.dto.car.CarDetailedResponseDto;
import app.carsharing.dto.car.CarResponseDto;
import app.carsharing.dto.car.CarUpdateRequestDto;
import app.carsharing.dto.car.CreateCarRequestDto;
import app.carsharing.model.Car;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CarMapper {
    Car toEntity(CreateCarRequestDto dto);

    CarResponseDto toDto(Car car);

    CarDetailedResponseDto toDetailedDto(Car car);

    void patchCar(@MappingTarget Car car, CarUpdateRequestDto dto);

    void updateCar(@MappingTarget Car car, CreateCarRequestDto dto);
}
