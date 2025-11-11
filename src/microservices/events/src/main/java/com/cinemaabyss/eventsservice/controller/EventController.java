package com.cinemaabyss.eventsservice.controllers;

import com.cinemaabyss.eventsservice.models.*;
import com.cinemaabyss.eventsservice.services.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping("/movie")
    public ResponseEntity<EventResponse> createMovieEvent(@Valid @RequestBody MovieEvent movieEvent) {
        log.info("Received request to create movie event: {}", movieEvent);
        EventResponse response = eventService.processMovieEvent(movieEvent);
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/user")
    public ResponseEntity<EventResponse> createUserEvent(@Valid @RequestBody UserEvent userEvent) {
        log.info("Received request to create user event: {}", userEvent);
        EventResponse response = eventService.processUserEvent(userEvent);
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/payment")
    public ResponseEntity<EventResponse> createPaymentEvent(@Valid @RequestBody PaymentEvent paymentEvent) {
        log.info("Received request to create payment event: {}", paymentEvent);
        EventResponse response = eventService.processPaymentEvent(paymentEvent);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        log.info("Health check requested");
        Map<String, Object> healthStatus = new HashMap<>();
        healthStatus.put("status", true);
        healthStatus.put("service", "events-service");
        healthStatus.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(healthStatus);
    }
}