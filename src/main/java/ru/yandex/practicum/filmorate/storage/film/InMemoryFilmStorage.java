package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.comparator.FilmRatingComparator;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.BaseStorage;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage extends BaseStorage<Film> implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private static final FilmRatingComparator filmRatingComparator = new FilmRatingComparator();

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public Optional<Film> getFilmById(Long filmId) {
        return Optional.ofNullable(films.get(filmId));
    }

    @Override
    public Film create(Film film) {
        long newId = getNextId(films);
        film.setId(newId);
        films.put(newId, film);
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        films.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    @Override
    public void like(Film film, Long userId) {
        film.getLikes().add(userId);
    }

    @Override
    public void removeLike(Film film, Long userId) {
        film.getLikes().remove(userId);
    }

    @Override
    public List<Film> getPopular(Integer count) {
        return films.values().stream()
                .sorted(filmRatingComparator.reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}