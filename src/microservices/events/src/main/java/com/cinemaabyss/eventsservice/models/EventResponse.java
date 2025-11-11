package com.cinemaabyss.eventsservice.models;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {
    @NotNull
    private String status;

    @NotNull
    private Integer partition;

    @NotNull
    private Long offset;

    @NotNull
    private Event event;
}