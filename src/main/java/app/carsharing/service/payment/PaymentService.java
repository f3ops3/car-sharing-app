package app.carsharing.service.payment;

import app.carsharing.dto.payment.PaymentDetailedResponseDto;
import app.carsharing.dto.payment.PaymentRequestDto;
import app.carsharing.dto.payment.PaymentResponseDto;
import app.carsharing.dto.payment.PaymentStatusResponseDto;
import app.carsharing.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentService {
    PaymentResponseDto createPaymentSession(User user, PaymentRequestDto paymentRequestDto);

    Page<PaymentDetailedResponseDto> getPayments(User user, Long id, Pageable pageable);

    PaymentStatusResponseDto handleSuccess(String sessionId);

    PaymentStatusResponseDto handleCancel(String sessionId);
}
