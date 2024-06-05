package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.comparator.FilmRatingComparator;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.BaseStorage;

import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage extends BaseStorage<Film> implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private static final LocalDate INVALID_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public Optional<Film> getFilmById(Long filmId) {
        checkId(filmId);
        return Optional.ofNullable(films.get(filmId));
    }

    @Override
    public Film create(Film film) {
        validateFilm(film);
        long newId = getNextId(films);
        film.setId(newId);
        films.put(newId, film);
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        checkId(newFilm.getId());
        validateFilm(newFilm);
        checkFilmExists(newFilm.getId());
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
        int maxSize = (count >= films.size()) ? films.size() : count;
        return films.values().stream()
                .sorted(new FilmRatingComparator().reversed())
                .limit(maxSize)
                .toList();
    }

    private void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(INVALID_RELEASE_DATE)
                || film.getReleaseDate().isAfter(LocalDate.now())) {
            throw new ValidationException("Неверная дата релиза");
        }
    }

    private void checkFilmExists(Long filmId) {
        if (!getFilmById(filmId).isPresent()) {
            log.error("Фильм с id: {} не найден", filmId);
            throw new NotFoundException("Фильм с id: " + filmId + " не найден");
        }
    }
}