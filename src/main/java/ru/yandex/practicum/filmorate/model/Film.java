package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Film {
    private Long id;
    @NotBlank
    private String name;
    private String description;
    private String releaseDate;
    @Positive
    private Integer duration;
}