package app.carsharing.dto.payment;

import app.carsharing.model.enums.Status;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PaymentStatusResponseDto {
    private String message;
    private String sessionId;
    private Status status;
}
