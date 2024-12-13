package app.carsharing.dto.car;

import lombok.Data;

@Data
public class CarResponseDto {
    private Long id;
    private String model;
    private String carType;
}
