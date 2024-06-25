package ru.yandex.practicum.filmorate.dto.film;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.Release;
import ru.yandex.practicum.filmorate.dto.genre.GenreFromNewOrUpdateFilmRequestDTO;
import ru.yandex.practicum.filmorate.dto.rating.RatingFromNewOrUpdateFilmRequestDTO;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class NewFilmRequestDTO {
    @NotBlank(message = "Поле не может быть пустым")
    private String name;
    @NotNull(message = "Обязательное поле")
    @Size(max = 200, message = "Должно быть до 200 символов")
    private String description;
    @NotNull(message = "Обязательное поле")
    @Release
    private LocalDate releaseDate;
    @NotNull(message = "Обязательное поле")
    @Positive(message = "Продолжительность должна быть положительной")
    private int duration;
    @NotNull(message = "Обязательное поле")
    private RatingFromNewOrUpdateFilmRequestDTO mpa;
    private List<GenreFromNewOrUpdateFilmRequestDTO> genres;
}