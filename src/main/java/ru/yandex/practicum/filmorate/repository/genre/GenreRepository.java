package ru.yandex.practicum.filmorate.repository.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface GenreRepository {
    Collection<Genre> getAllGenres();

    Optional<Genre> getGenreById(int genreId);

    Set<Integer> getGenresByFilm(Integer filmId);
}