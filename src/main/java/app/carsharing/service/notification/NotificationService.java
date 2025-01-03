package app.carsharing.service.notification;

public interface NotificationService {
    void sentNotification(Long chatId, String message);
}
