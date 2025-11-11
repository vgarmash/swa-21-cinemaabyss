package com.cinemaabyss.eventsservice.services;

import com.cinemaabyss.eventsservice.models.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {

    private final StreamBridge streamBridge;

    public EventResponse processMovieEvent(MovieEvent movieEvent) {
        log.info("Processing movie event: {}", movieEvent);

        Event event = new Event(
                generateEventId("movie", movieEvent.getMovieId()),
                "movie",
                LocalDateTime.now(),
                movieEvent
        );

        boolean sent = streamBridge.send("movieEventsOutput", event);

        if (sent) {
            log.info("Movie event sent successfully to Kafka");
            return new EventResponse("success", 0, System.currentTimeMillis(), event);
        } else {
            log.error("Failed to send movie event to Kafka");
            return new EventResponse("error", -1, -1L, event);
        }
    }

    public EventResponse processUserEvent(UserEvent userEvent) {
        log.info("Processing user event: {}", userEvent);

        Event event = new Event(
                generateEventId("user", userEvent.getUserId()),
                "user",
                userEvent.getTimestamp(),
                userEvent
        );

        boolean sent = streamBridge.send("userEventsOutput", event);

        if (sent) {
            log.info("User event sent successfully to Kafka");
            return new EventResponse("success", 0, System.currentTimeMillis(), event);
        } else {
            log.error("Failed to send user event to Kafka");
            return new EventResponse("error", -1, -1L, event);
        }
    }

    public EventResponse processPaymentEvent(PaymentEvent paymentEvent) {
        log.info("Processing payment event: {}", paymentEvent);

        Event event = new Event(
                generateEventId("payment", paymentEvent.getPaymentId()),
                "payment",
                paymentEvent.getTimestamp(),
                paymentEvent
        );

        boolean sent = streamBridge.send("paymentEventsOutput", event);

        if (sent) {
            log.info("Payment event sent successfully to Kafka");
            return new EventResponse("success", 0, System.currentTimeMillis(), event);
        } else {
            log.error("Failed to send payment event to Kafka");
            return new EventResponse("error", -1, -1L, event);
        }
    }

    private String generateEventId(String type, Integer id) {
        return type + "-" + id + "-" + UUID.randomUUID().toString().substring(0, 8);
    }
}