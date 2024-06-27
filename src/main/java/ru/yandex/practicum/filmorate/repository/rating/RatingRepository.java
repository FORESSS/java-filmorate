package ru.yandex.practicum.filmorate.repository.rating;

import ru.yandex.practicum.filmorate.model.Rating;

import java.util.Collection;
import java.util.Optional;

public interface RatingRepository {
    Collection<Rating> getAllRatings();

    Optional<Rating> getRatingById(int ratingId);
}