package mate.academy.bookingapp.service.notification;

import io.github.cdimascio.dotenv.Dotenv;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TelegramNotificationService implements NotificationService {

    private final String chatId;
    private final String telegramApiUrl;
    private final RestTemplate restTemplate;

    public TelegramNotificationService() {
        Dotenv dotenv = Dotenv.load();

        String botToken = dotenv.get("TELEGRAM_BOT_TOKEN");
        this.chatId = dotenv.get("TELEGRAM_CHAT_ID");

        if (botToken == null || chatId == null) {
            throw new RuntimeException("Missing TELEGRAM_BOT_TOKEN or TELEGRAM_CHAT_ID in .env");
        }

        this.telegramApiUrl = "https://api.telegram.org/bot" + botToken + "/sendMessage";
        this.restTemplate = new RestTemplate();
    }

    public void sendNotification(String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> payload = new HashMap<>();
        payload.put("chat_id", chatId);
        payload.put("text", message);
        payload.put("parse_mode", "Markdown");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        try {
            restTemplate.postForEntity(telegramApiUrl, request, String.class);
        } catch (Exception e) {
            System.err.println("Failed to send Telegram message: " + e.getMessage());
        }
    }
}
