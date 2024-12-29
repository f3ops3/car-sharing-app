package app.carsharing.service.payment.impl;

import app.carsharing.exception.PaymentException;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import java.math.BigDecimal;
import java.time.Instant;
import org.springframework.stereotype.Service;

@Service
public class StripeService {
    private static final String DOMAIN = "http://localhost:8080";
    private static final String CANCELED_LINK = "/payments/success/{CHECKOUT_SESSION_ID}";
    private static final String SUCCESS_LINK = "/payments/cancel/{CHECKOUT_SESSION_ID}";
    private static final String SESSION_NAME = "Car Rental Payment";
    private static final String CURRENCY = "USD";
    private static final int HOUR_IN_SECONDS = 3600;
    private static final BigDecimal CENTS_AMOUNT = BigDecimal.valueOf(100);
    private static final long DEFAULT_QUANTITY = 1;

    public Session createSession(BigDecimal amountToPay) {
        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setExpiresAt(Instant.now().getEpochSecond() + HOUR_IN_SECONDS)
                        .setSuccessUrl(DOMAIN + SUCCESS_LINK)
                        .setCancelUrl(DOMAIN + CANCELED_LINK)
                        .addLineItem(
                                SessionCreateParams.LineItem.builder()
                                        .setQuantity(DEFAULT_QUANTITY)
                                        .setPriceData(SessionCreateParams.LineItem.PriceData
                                                .builder()
                                                .setCurrency(CURRENCY)
                                                .setUnitAmountDecimal(
                                                        amountToPay.multiply(CENTS_AMOUNT)
                                                )
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData
                                                                .ProductData.builder()
                                                                .setName(SESSION_NAME)
                                                                .build()
                                                )
                                                .build())
                                        .build())
                        .build();
        try {
            return Session.create(params);
        } catch (StripeException ex) {
            throw new PaymentException("Cant create stripe session");
        }
    }
}
