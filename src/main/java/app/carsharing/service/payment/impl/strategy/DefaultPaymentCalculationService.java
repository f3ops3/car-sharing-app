package app.carsharing.service.payment.impl.strategy;

import static java.time.temporal.ChronoUnit.DAYS;

import app.carsharing.model.Rental;
import app.carsharing.model.enums.Type;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public class DefaultPaymentCalculationService implements PaymentCalculationService {
    private static final String PAYMENT_TYPE = Type.PAYMENT.name();

    @Override
    public String getPaymentType() {
        return PAYMENT_TYPE;
    }

    @Override
    public BigDecimal calculatePayment(Rental rental) {
        long days = DAYS.between(rental.getRentalDate(), rental.getReturnDate());
        return rental.getCar().getDailyFee().multiply(BigDecimal.valueOf(days));
    }
}
