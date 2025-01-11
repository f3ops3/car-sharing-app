package app.carsharing.dto.payment;

import app.carsharing.model.enums.Status;
import app.carsharing.model.enums.Type;
import java.math.BigDecimal;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PaymentDetailedResponseDto {
    private Long id;
    private String sessionUrl;
    private String sessionId;
    private BigDecimal amountToPay;
    private Long rentalId;
    private Status status;
    private Type type;
}
