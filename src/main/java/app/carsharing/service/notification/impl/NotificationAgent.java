package app.carsharing.service.notification.impl;

import app.carsharing.dto.payment.PaymentStatusResponseDto;
import app.carsharing.dto.rental.RentalDetailedDto;
import app.carsharing.exception.NotificationException;
import app.carsharing.model.Rental;
import app.carsharing.model.User;
import app.carsharing.service.notification.NotificationService;
import app.carsharing.util.Message;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationAgent {
    private static final Logger logger = LogManager.getLogger(NotificationAgent.class);
    private final NotificationService notificationService;

    public <T> void notifyAsync(User user, T object) {
        if (user.getTgChatId() != null) {
            CompletableFuture.runAsync(() -> notify(user.getTgChatId(), object))
                    .exceptionally(ex -> {
                        logger.error("Failed to send notification: " + ex.getMessage());
                        return null;
                    });
        } else {
            logger.error("The user with id " + user.getId()
                    + " does not have their Telegram chat id set");
        }
    }

    private <T> void notify(Long chatId, T object) {
        String message = getMessage(object);
        try {
            notificationService.sendNotification(chatId, message);
        } catch (NotificationException e) {
            logger.error("Failed to send notification to Telegram for user with chat id {}: {}",
                    chatId, e.getMessage());
        }
    }

    private <T> String getMessage(T object) {
        if (object instanceof RentalDetailedDto rental) {
            return Message.getRentalMessageForCustomer(rental);
        } else if (object instanceof PaymentStatusResponseDto response) {
            return response.getMessage();
        } else if (object instanceof Rental rental) {
            return Message.getOverdueRentalMessageForCustomer(rental);
        } else if (object instanceof String str) {
            return str;
        } else {
            throw new IllegalArgumentException("The object " + object.getClass()
                    + " does not fit any notification form");
        }
    }
}
