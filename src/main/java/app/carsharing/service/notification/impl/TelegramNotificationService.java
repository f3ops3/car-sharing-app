package app.carsharing.service.notification.impl;

import app.carsharing.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramNotificationService implements NotificationService {
    private final TelegramBot notificationBot;

    @Override
    public void sentNotification(Long chatId, String message) {
        notificationBot.sendMessage(chatId, message);
    }
}
