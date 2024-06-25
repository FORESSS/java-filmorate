package ru.yandex.practicum.filmorate.dto.film;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.dto.genre.GenreDTO;
import ru.yandex.practicum.filmorate.dto.rating.RatingDTO;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class FilmDTO {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private RatingDTO mpa;
    private List<GenreDTO> genres;
}