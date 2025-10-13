package com.cinemaabyss.eventsservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Slf4j
@Configuration
public class EventStreams {

    @Bean
    public Consumer<String> movieEventsInput() {
        return message -> {
            log.info("Received Movie Event from Kafka: {}", message);
            // Здесь можно добавить бизнес-логику обработки movie events
            processMovieEvent(message);
        };
    }

    @Bean
    public Consumer<String> userEventsInput() {
        return message -> {
            log.info("Received User Event from Kafka: {}", message);
            // Здесь можно добавить бизнес-логику обработки user events
            processUserEvent(message);
        };
    }

    @Bean
    public Consumer<String> paymentEventsInput() {
        return message -> {
            log.info("Received Payment Event from Kafka: {}", message);
            // Здесь можно добавить бизнес-логику обработки payment events
            processPaymentEvent(message);
        };
    }

    private void processMovieEvent(String message) {
        // Реализация обработки события фильма
        log.debug("Processing movie event: {}", message);
    }

    private void processUserEvent(String message) {
        // Реализация обработки события пользователя
        log.debug("Processing user event: {}", message);
    }

    private void processPaymentEvent(String message) {
        // Реализация обработки события платежа
        log.debug("Processing payment event: {}", message);
    }
}