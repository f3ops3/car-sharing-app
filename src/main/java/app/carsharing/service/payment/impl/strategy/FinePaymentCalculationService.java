package app.carsharing.service.payment.impl.strategy;

import static java.time.temporal.ChronoUnit.DAYS;

import app.carsharing.model.Rental;
import app.carsharing.model.enums.Type;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public class FinePaymentCalculationService implements PaymentCalculationService {
    private static final String PAYMENT_TYPE = Type.FINE.name();
    private static final BigDecimal FINE_MULTIPLIER = new BigDecimal("1.5");

    @Override
    public String getPaymentType() {
        return PAYMENT_TYPE;
    }

    @Override
    public BigDecimal calculatePayment(Rental rental) {
        long days = DAYS.between(rental.getRentalDate(), rental.getActualReturnDate());
        return rental.getCar().getDailyFee()
                .multiply(BigDecimal.valueOf(days))
                .multiply(FINE_MULTIPLIER);
    }
}
