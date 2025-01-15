package app.carsharing.controller;

import app.carsharing.dto.payment.PaymentDetailedResponseDto;
import app.carsharing.dto.payment.PaymentRequestDto;
import app.carsharing.dto.payment.PaymentResponseDto;
import app.carsharing.dto.payment.PaymentStatusResponseDto;
import app.carsharing.model.User;
import app.carsharing.service.notification.impl.NotificationAgent;
import app.carsharing.service.payment.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Car sharing", description = "Endpoints for payments management")
@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final NotificationAgent notificationAgent;

    @Operation(summary = "Create new payment")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponseDto createPayment(@AuthenticationPrincipal User user,
                                            @RequestBody @Valid PaymentRequestDto requestDto) {
        return paymentService.createPaymentSession(user, requestDto);
    }

    @Operation(summary = "Get specific payment by id")
    @GetMapping("/{id}")
    public Page<PaymentDetailedResponseDto> getPayment(@AuthenticationPrincipal User user,
                                                       @PathVariable Long id,
                                                       Pageable pageable) {
        return paymentService.getPayments(user, id, pageable);
    }

    @Operation(summary = "Handle success payment")
    @GetMapping("/success/{sessionId}")
    public PaymentStatusResponseDto handleSuccess(@AuthenticationPrincipal User user,
                                                  @PathVariable String sessionId) {
        PaymentStatusResponseDto responseDto = paymentService.handleSuccess(user, sessionId);
        notificationAgent.notifyAsync(user, responseDto);
        return responseDto;
    }

    @Operation(summary = "Handle cancel payment")
    @GetMapping("/cancel/{sessionId}")
    public PaymentStatusResponseDto handleCancel(@PathVariable String sessionId) {
        return paymentService.handleCancel(sessionId);
    }
}
