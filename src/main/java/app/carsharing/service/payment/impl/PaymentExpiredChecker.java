package app.carsharing.service.payment.impl;

import app.carsharing.exception.PaymentException;
import app.carsharing.model.Payment;
import app.carsharing.model.enums.Status;
import app.carsharing.repository.payment.PaymentRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentExpiredChecker {
    private static final String EXPIRED = "expired";
    private final PaymentRepository paymentRepository;

    @Scheduled(fixedRate = 60000)
    private void checkPaymentsExpired() {
        List<Payment> pendingPayments = paymentRepository
                .findByStatus(Status.PENDING);
        if (!pendingPayments.isEmpty()) {
            for (Payment payment : pendingPayments) {
                updatePaymentStatus(payment);
            }
        }
    }

    private void updatePaymentStatus(Payment payment) {
        try {
            Session session = Session.retrieve(payment.getSessionId());
            String sessionStatus = session.getStatus();
            if (sessionStatus.equals(EXPIRED)) {
                payment.setStatus(Status.EXPIRED);
                paymentRepository.save(payment);
            }
        } catch (StripeException e) {
            throw new PaymentException("Error while checking payment for expiration");
        }
    }
}
