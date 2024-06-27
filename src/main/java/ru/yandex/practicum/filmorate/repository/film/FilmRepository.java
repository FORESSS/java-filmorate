package ru.yandex.practicum.filmorate.repository.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmRepository {
    Collection<Film> getAllFilms();

    Film getFilmById(int filmId);

    Film addFilm(Film film);

    Film updateFilm(Film film);

    void addUserLikeToFilm(Integer filmId, Integer userId);

    void deleteUserLikeFromFilm(Integer filmId, Integer userId);

    boolean containsFilmById(int filmId);

    boolean containsFilmByValue(Film film);

    boolean containsUserLikeForFilm(Integer filmId, Integer userId);

    int getFilmLikes(Integer filmId);

    Collection<Film> getMostPopularFilms(int count);
}