package app.carsharing.dto.payment;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PaymentRequestDto {
    @Positive
    private Long rentalId;
    private String paymentType;
}
