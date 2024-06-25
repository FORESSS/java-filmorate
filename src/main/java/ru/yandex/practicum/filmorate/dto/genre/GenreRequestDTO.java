package ru.yandex.practicum.filmorate.dto.genre;

import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenreRequestDTO {
    @Positive(message = "ID должен быть положительным числом")
    private int id;
    private String name;
}