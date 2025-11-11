package com.cinemaabyss.eventsservice.models;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEvent {
    @NotNull
    private Integer paymentId;

    @NotNull
    private Integer userId;

    @NotNull
    private Double amount;

    @NotNull
    private String status;

    @NotNull
    private LocalDateTime timestamp;

    private String methodType;
}