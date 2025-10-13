package com.cinemaabyss.eventsservice.models;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEvent {
    @NotNull
    private Integer userId;

    private String username;
    private String email;

    @NotNull
    private String action;

    @NotNull
    private LocalDateTime timestamp;
}