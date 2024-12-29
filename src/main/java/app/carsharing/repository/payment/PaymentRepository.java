package app.carsharing.repository.payment;

import app.carsharing.model.Payment;
import app.carsharing.model.enums.Status;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByRentalIdAndStatusIn(Long id, List<Status> statusList);

    Page<Payment> findAllByRentalUserId(Long id, Pageable pageable);

    Optional<Payment> findBySessionId(String sessionId);
}
