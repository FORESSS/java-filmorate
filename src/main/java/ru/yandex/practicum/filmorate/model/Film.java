package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class Film {
    private int id;
    private String name;
    private String description;
    private Set<Integer> genresId;
    private Integer mpaId;
    private LocalDate releaseDate;
    private int duration;
}