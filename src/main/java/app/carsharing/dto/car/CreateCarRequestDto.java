package app.carsharing.dto.car;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateCarRequestDto {
    @NotBlank
    private String model;
    @NotBlank
    private String brand;
    @NotBlank
    private String carType;
    @NotNull
    @Positive
    private int inventory;
    @NotNull
    @Min(0)
    private BigDecimal dailyFee;
}
