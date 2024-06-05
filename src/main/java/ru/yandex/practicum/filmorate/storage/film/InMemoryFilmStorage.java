package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.comparator.FilmRatingComparator;
import ru.yandex.practicum.filmorate.exception.InvalidRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.BaseStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage extends BaseStorage<Film> implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private static final LocalDate INVALID_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private static final FilmRatingComparator filmRatingComparator = new FilmRatingComparator();

    @Override
    public Collection<Film> getAllFilms() {
        log.info("Возврат списка фильмов");
        return films.values();
    }

    @Override
    public Optional<Film> getFilmById(Long filmId) {
        checkId(filmId);
        log.info("Возврат фильма с id: {}", filmId);
        return Optional.ofNullable(films.get(filmId));
    }

    @Override
    public Film create(Film film) {
        validateFilm(film);
        long newId = getNextId(films);
        film.setId(newId);
        films.put(newId, film);
        log.info("Добавлен фильм с id: {}", film.getId());
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        checkId(newFilm.getId());
        validateFilm(newFilm);
        checkFilmExists(newFilm.getId());
        films.put(newFilm.getId(), newFilm);
        log.info("Изменен фильм с id: {}", newFilm.getId());
        return newFilm;
    }

    @Override
    public void like(Film film, Long userId) {
        film.getLikes().add(userId);
        log.info("Пользователь с id: {} поставил лайк фильму с id: {}", userId, film.getId());
    }

    @Override
    public void removeLike(Film film, Long userId) {
        film.getLikes().remove(userId);
        log.info("Пользователь с id: {} удалил лайк у фильма с id: {}", userId, film.getId());
    }

    @Override
    public List<Film> getPopular(Integer count) {
        if (count > films.size()) {
            log.error("Превышено количество популярных фильмов в списке: {}", count);
            throw new InvalidRequestException("Превышено количество популярных фильмов в списке");
        }
        log.info("Возврат списка популярных фильмов");
        return films.values().stream()
                .sorted(filmRatingComparator.reversed())
                .limit(count)
                .collect(Collectors.toList());
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