package app.carsharing.mapper;

import app.carsharing.config.MapperConfig;
import app.carsharing.dto.payment.PaymentDetailedResponseDto;
import app.carsharing.dto.payment.PaymentResponseDto;
import app.carsharing.dto.payment.PaymentStatusResponseDto;
import app.carsharing.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface PaymentMapper {
    @Mapping(target = "sessionUrl", source = "session")
    PaymentResponseDto toDto(Payment payment);

    @Mapping(target = "sessionUrl", source = "session")
    @Mapping(target = "rentalId", source = "rental.id")
    PaymentDetailedResponseDto toDetailedDto(Payment payment);

    PaymentStatusResponseDto toStatusDto(Payment payment);
}
