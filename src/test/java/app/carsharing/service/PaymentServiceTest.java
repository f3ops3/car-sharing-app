package app.carsharing.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import app.carsharing.dto.payment.PaymentDetailedResponseDto;
import app.carsharing.dto.payment.PaymentStatusResponseDto;
import app.carsharing.mapper.PaymentMapper;
import app.carsharing.model.Payment;
import app.carsharing.model.Rental;
import app.carsharing.model.User;
import app.carsharing.model.enums.Role;
import app.carsharing.model.enums.Status;
import app.carsharing.repository.payment.PaymentRepository;
import app.carsharing.service.payment.impl.PaymentServiceImpl;
import app.carsharing.util.TestUtil;
import jakarta.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(value = MockitoExtension.class)
public class PaymentServiceTest {
    private static final String PAYMENT_CANCELED_MESSAGE = "Payment session canceled";
    private static final String PAYMENT_NOT_FOUND_BY_SESSION_EXCEPTION =
            "Can't find payment by sessionId: %s";
    private static final Role USER_ROLE = Role.CUSTOMER;
    private static final User USER = TestUtil.getUser(USER_ROLE);
    private static final Rental RENTAL = TestUtil.getRental(USER);
    private static final Payment PAYMENT = TestUtil.getPayment(RENTAL);
    private static final PaymentDetailedResponseDto PAYMENT_FULL_RESPONSE_DTO =
            TestUtil.paymentDetailedResponseDto(PAYMENT);

    private static final PaymentStatusResponseDto PAYMENT_STATUS_RESPONSE_DTO =
            new PaymentStatusResponseDto()
                    .setStatus(PAYMENT.getStatus())
                    .setSessionId(PAYMENT.getSessionId())
                    .setMessage(PAYMENT_CANCELED_MESSAGE);
    @Mock
    private PaymentMapper paymentMapper;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    @DisplayName("Find all payments by valid user should return a page of payments")
    void findAllByUserId_withValidUser_ShouldReturnPayments() {
        Pageable pageable = PageRequest.of(0, 10);
        when(paymentRepository.findAllByRentalUserId(RENTAL.getUser().getId(), pageable))
                .thenReturn(new PageImpl<>(Collections.singletonList(PAYMENT)));
        when(paymentMapper.toDetailedDto(PAYMENT)).thenReturn(PAYMENT_FULL_RESPONSE_DTO);

        Page<PaymentDetailedResponseDto> actual = paymentService.getPayments(USER, 1L, pageable);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(new PageImpl<>(
                Collections.singletonList(PAYMENT_FULL_RESPONSE_DTO)), actual
        );
        verify(paymentRepository, times(1)).findAllByRentalUserId(1L, pageable);
        verify(paymentMapper, times(1)).toDetailedDto(PAYMENT);
    }

    @Test
    @DisplayName("Handle cancel payment with valid data should return a message")
    void handleCancel_withValidData_ShouldReturnCanceledMessage() {
        String sessionId = "someSessionId";

        when(paymentRepository.findBySessionId(sessionId)).thenReturn(Optional.of(PAYMENT));
        when(paymentRepository.save(PAYMENT))
                .thenReturn(PAYMENT.setStatus(Status.CANCELED));
        when(paymentMapper.toStatusDto(PAYMENT)).thenReturn(PAYMENT_STATUS_RESPONSE_DTO);
        PaymentStatusResponseDto actual = paymentService.handleCancel(sessionId);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(PAYMENT_STATUS_RESPONSE_DTO, actual);
        verify(paymentRepository, times(1)).findBySessionId(sessionId);
        verify(paymentRepository, times(1)).save(PAYMENT);
        verify(paymentMapper, times(1)).toStatusDto(PAYMENT);
    }

    @Test
    @DisplayName("Handle cancel payment with invalid data should throw EntityNotFoundException")
    void handleCancel_withInvalidSessionId_ShouldThrowException() {
        String sessionId = "someSessionId";

        when(paymentRepository.findBySessionId(sessionId))
                .thenReturn(Optional.empty());

        String actual = Assertions.assertThrows(EntityNotFoundException.class,
                () -> paymentService.handleCancel(sessionId)).getMessage();
        String expected = String.format(PAYMENT_NOT_FOUND_BY_SESSION_EXCEPTION, sessionId);

        Assertions.assertEquals(expected, actual);
        verify(paymentRepository, times(1)).findBySessionId(sessionId);
    }
}
