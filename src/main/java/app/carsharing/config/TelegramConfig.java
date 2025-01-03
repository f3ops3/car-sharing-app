package app.carsharing.config;

import app.carsharing.service.notification.impl.TelegramBot;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class TelegramConfig {
    private String botToken;
    private String botName;

    public TelegramConfig() {
        Dotenv dotenv = Dotenv.load();
        botToken = dotenv.get("TELEGRAM_BOT_TOKEN");
        botName = dotenv.get("TELEGRAM_BOT_NAME");
    }

    @Bean
    public TelegramBot getTelegramBot() {
        return new TelegramBot(botToken, botName);
    }

    @Bean
    public TelegramBotsApi getTelegramBotsApi(TelegramBot telegramBot) throws TelegramApiException {
        if (botToken.isEmpty() || botName.isEmpty()) {
            return null;
        }
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(telegramBot);
        return botsApi;
    }
}
