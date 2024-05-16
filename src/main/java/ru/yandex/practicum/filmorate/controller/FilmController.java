package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController extends BaseController<Film> {
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Возврат списка фильмов");
        return films.values();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        checkReleaseDate(film);
        film.setId(getNextId(films));
        log.info("Добавлен фильм с id: {}", film.getId());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        checkId(film);
        checkReleaseDate(film);
        log.info("Изменен фильм с id: {}", film.getId());
        films.put(film.getId(), film);
        return film;
    }

    private void checkId(Film film) {
        if (film.getId() == null) {
            log.error("Не указан id");
            throw new ValidationException("Не указан id");
        } else if (!films.containsKey(film.getId())) {
            log.error("Фильм с id: {} не найден", film.getId());
            throw new ValidationException("Фильм с id: " + film.getId() + " не найден");
        }
    }

    private void checkReleaseDate(Film film) {
        if (Instant.parse(film.getReleaseDate() + "T00:00:00Z").isBefore(Instant.parse("1895-12-28T00:00:00Z"))) {
            throw new ValidationException("Неверная дата релиза");
        }
    }
}