package app.carsharing.controller;

import app.carsharing.dto.payment.PaymentDetailedResponseDto;
import app.carsharing.dto.payment.PaymentRequestDto;
import app.carsharing.dto.payment.PaymentResponseDto;
import app.carsharing.dto.payment.PaymentStatusResponseDto;
import app.carsharing.model.User;
import app.carsharing.service.payment.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponseDto createPayment(@AuthenticationPrincipal User user,
                                            @RequestBody @Valid PaymentRequestDto requestDto) {
        return paymentService.createPaymentSession(user, requestDto);
    }

    @GetMapping("/{id}")
    public Page<PaymentDetailedResponseDto> getPayment(@AuthenticationPrincipal User user,
                                                       @PathVariable Long id,
                                                       Pageable pageable) {
        return paymentService.getPayments(user, id, pageable);
    }

    @GetMapping("/success/{sessionId}")
    public PaymentStatusResponseDto handleSuccess(@PathVariable String sessionId) {
        return paymentService.handleSuccess(sessionId);
    }

    @GetMapping("/cancel/{sessionId}")
    public PaymentStatusResponseDto handleCancel(@PathVariable String sessionId) {
        return paymentService.handleCancel(sessionId);
    }
}
