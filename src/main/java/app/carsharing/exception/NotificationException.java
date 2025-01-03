package app.carsharing.exception;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class NotificationException extends RuntimeException {
    public NotificationException(String message) {
        super(message);
    }

    public NotificationException(String message, TelegramApiException cause) {
        super(message, cause);
    }
}
