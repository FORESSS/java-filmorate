package ru.yandex.practicum.filmorate.dto.rating;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RatingDTO {
    private int id;
    private String name;
}