package ru.yandex.practicum.filmorate.dto.genre;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenreDTO {
    private int id;
    private String name;
}