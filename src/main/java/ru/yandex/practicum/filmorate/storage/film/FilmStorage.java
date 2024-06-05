package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Collection<Film> getAllFilms();

    Optional<Film> getFilmById(Long filmId);

    Film create(Film film);

    Film update(Film newFilm);

    void like(Film film, Long userId);

    void removeLike(Film film, Long userId);

    List<Film> getPopular(Integer count);
}