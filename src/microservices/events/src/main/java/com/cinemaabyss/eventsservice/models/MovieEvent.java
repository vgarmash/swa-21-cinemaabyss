package com.cinemaabyss.eventsservice.models;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieEvent {
    @NotNull
    private Integer movieId;

    @NotNull
    private String title;

    @NotNull
    private String action;

    private Integer userId;
    private Double rating;
    private List<String> genres;
    private String description;
}