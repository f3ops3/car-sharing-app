package app.carsharing.service.payment.impl;

import app.carsharing.dto.payment.PaymentDetailedResponseDto;
import app.carsharing.dto.payment.PaymentRequestDto;
import app.carsharing.dto.payment.PaymentResponseDto;
import app.carsharing.dto.payment.PaymentStatusResponseDto;
import app.carsharing.exception.PaymentException;
import app.carsharing.mapper.PaymentMapper;
import app.carsharing.model.Payment;
import app.carsharing.model.Rental;
import app.carsharing.model.User;
import app.carsharing.model.enums.Status;
import app.carsharing.model.enums.Type;
import app.carsharing.repository.car.CarRepository;
import app.carsharing.repository.payment.PaymentRepository;
import app.carsharing.repository.rental.RentalRepository;
import app.carsharing.service.payment.PaymentService;
import app.carsharing.service.payment.impl.strategy.PaymentCalculationStrategy;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private static final String COMPLETE_SESSION_STATUS = "complete";
    private final StripeService stripeService;
    private final RentalRepository rentalRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentCalculationStrategy paymentCalculationStrategy;
    private final PaymentMapper paymentMapper;
    private final CarRepository carRepository;

    @Override
    public PaymentResponseDto createPaymentSession(User user,
                                                   PaymentRequestDto paymentRequestDto) {
        if (paymentRepository.findByRentalIdAndStatusIn(paymentRequestDto.getRentalId(),
                List.of(Status.PAID, Status.PENDING)).isPresent()) {
            throw new PaymentException("Payment already exists");
        }
        Rental rental = rentalRepository.findById(paymentRequestDto.getRentalId()).orElseThrow(
                () -> new EntityNotFoundException("Rental with id: "
                        + paymentRequestDto.getRentalId() + " not found")
        );
        rental.setCar(carRepository.findById(rental.getCar().getId()).orElseThrow(
                () -> new EntityNotFoundException("Car with id: " + rental.getCar().getId())
        ));
        if (!rental.getUser().getId().equals(user.getId())) {
            throw new PaymentException("You don't have permission to rental with id: "
                    + rental.getId());
        }
        String paymentRequestType = paymentRequestDto.getPaymentType();
        BigDecimal amountToPay = paymentCalculationStrategy
                .getCalculationService(paymentRequestType.toUpperCase())
                .calculatePayment(rental);
        Session session = stripeService.createSession(amountToPay);
        Payment payment = new Payment()
                .setRental(rental)
                .setAmountToPay(amountToPay)
                .setSessionId(session.getId())
                .setSession(session.getUrl())
                .setStatus(Status.PENDING)
                .setType(Type.valueOf(paymentRequestType));
        return paymentMapper.toDto(paymentRepository.save(payment));
    }

    @Override
    public Page<PaymentDetailedResponseDto> getPayments(User user, Long id, Pageable pageable) {
        boolean isAdmin = user.getAuthorities()
                .stream()
                .anyMatch(auth -> "ROLE_ADMIN".equals(auth.getAuthority()));
        Long searchableId = id;
        if (!isAdmin && !user.getId().equals(id)) {
            searchableId = user.getId();
        }
        return paymentRepository.findAllByRentalUserId(searchableId, pageable)
                .map(paymentMapper::toDetailedDto);
    }

    @Transactional
    @Override
    public PaymentStatusResponseDto handleSuccess(String sessionId) {
        try {
            Payment payment = paymentRepository.findBySessionId(sessionId).orElseThrow(
                    () -> new EntityNotFoundException("Can't find payment by "
                            + "sessionId: " + sessionId)
            );
            Session session = Session.retrieve(sessionId);
            if (session.getStatus().equals(COMPLETE_SESSION_STATUS)) {
                payment.setStatus(Status.PAID);
                paymentRepository.save(payment);
                return paymentMapper.toStatusDto(payment).setMessage("Successful payment");
            }
            return paymentMapper.toStatusDto(payment).setMessage("Unsuccessful payment");
        } catch (StripeException e) {
            throw new PaymentException("Stripe error with session: " + sessionId);
        }
    }

    @Override
    public PaymentStatusResponseDto handleCancel(String sessionId) {
        Payment payment = paymentRepository.findBySessionId(sessionId).orElseThrow(
                () -> new EntityNotFoundException("Can't find payment by sessionId: " + sessionId)
        );
        payment.setStatus(Status.CANCELED);
        return paymentMapper.toStatusDto(paymentRepository.save(payment))
                .setMessage("Payment session canceled");
    }
}
