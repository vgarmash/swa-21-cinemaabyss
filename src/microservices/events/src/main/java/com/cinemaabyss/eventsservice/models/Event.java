package com.cinemaabyss.eventsservice.models;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @NotNull
    private String id;

    @NotNull
    private String type;

    @NotNull
    private LocalDateTime timestamp;

    @NotNull
    private Object payload;
}