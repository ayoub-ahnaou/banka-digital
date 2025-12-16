package com.digital.banka;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Objects;

@SpringBootApplication
public class BankaDigitalApplication {

    public static void main(String[] args) {
        // Load .env first
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        System.setProperty("DB_HOST", Objects.requireNonNull(dotenv.get("DB_HOST")));
        System.setProperty("DB_PORT", Objects.requireNonNull(dotenv.get("DB_PORT")));
        System.setProperty("DB_NAME", Objects.requireNonNull(dotenv.get("DB_NAME")));
        System.setProperty("DB_USER", Objects.requireNonNull(dotenv.get("DB_USER")));
        System.setProperty("DB_PASSWORD", Objects.requireNonNull(dotenv.get("DB_PASSWORD")));

        System.setProperty("JWT_SECRET", Objects.requireNonNull(dotenv.get("JWT_SECRET")));
        System.setProperty("JWT_EXPIRATION", Objects.requireNonNull(dotenv.get("JWT_EXPIRATION")));
        System.setProperty("JWT_REFRESH_EXPIRATION", Objects.requireNonNull(dotenv.get("JWT_REFRESH_EXPIRATION")));

        SpringApplication.run(BankaDigitalApplication.class, args);
    }

}
