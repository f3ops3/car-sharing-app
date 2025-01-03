package app.carsharing.service.notification.impl;

import app.carsharing.exception.NotificationException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Getter
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {
    private final String botToken;
    private final String botName;

    public void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage(chatId.toString(), text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new NotificationException("There was an error during "
                    + "sending a message: " + text, e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().getText().equals("/getchatid")) {
            Long chatId = update.getMessage().getChatId();
            SendMessage message = new SendMessage();
            message.setChatId(chatId.toString());
            message.setText("Your Chat ID: " + chatId);

            try {
                execute(message);
            } catch (TelegramApiException e) {
                throw new NotificationException("Can't send telegram chatId to user");
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }
}
