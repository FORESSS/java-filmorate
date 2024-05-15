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
        log.info("Возврат списка фильмов на эндпоинт GET /films");
        return films.values();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        checkFilm(film);
        film.setId(getNextId(films));
        log.info("Добавлен фильм с id={}", film.getId());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        checkFilm(film);
        if (film.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        } else if (!films.containsKey(film.getId())) {
            throw new ValidationException("film c id=" + film.getId() + " не найден");
        }
        log.info("Изменен фильм с id={}", film.getId());
        films.put(film.getId(), film);
        return films.get(film.getId());
    }

    public void checkFilm(Film film) {
        if (film == null) {
            throw new ValidationException("film equal null");
        } else if (film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов");
        } else if (Instant.parse(film.getReleaseDate() + "T00:00:00Z").isBefore(Instant.parse("1895-12-28T00:00:00Z"))) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
    }
}