package mate.academy.bookingapp.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramConfig {
    @Bean
    public Dotenv dotenv() {
        return Dotenv.load();
    }
}
