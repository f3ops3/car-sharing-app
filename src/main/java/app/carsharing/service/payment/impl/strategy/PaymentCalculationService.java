package app.carsharing.service.payment.impl.strategy;

import app.carsharing.model.Rental;
import java.math.BigDecimal;

public interface PaymentCalculationService {
    String getPaymentType();

    BigDecimal calculatePayment(Rental rental);
}
