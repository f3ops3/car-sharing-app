package app.carsharing.service.payment.impl.strategy;

import app.carsharing.exception.PaymentException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentCalculationStrategy {
    private final List<PaymentCalculationService> paymentCalculationServices;

    public PaymentCalculationService getCalculationService(String paymentType) {
        return paymentCalculationServices.stream()
                .filter(paymentCalculationService -> paymentCalculationService
                        .getPaymentType().equals(paymentType))
                .findFirst()
                .orElseThrow(
                        () -> new PaymentException("No payment service found "
                                + "for type: " + paymentType)
                );
    }
}
