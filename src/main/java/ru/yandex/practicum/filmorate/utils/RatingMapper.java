package ru.yandex.practicum.filmorate.utils;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.rating.RatingDTO;
import ru.yandex.practicum.filmorate.model.Rating;

@Component
public class RatingMapper {
    public static RatingDTO ratingToDTO(Rating rating) {
        return RatingDTO.builder()
                .id(rating.getId())
                .name(rating.getName())
                .build();
    }
}