package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
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
    public ResponseEntity<Film> addFilm(@Valid @RequestBody Film film) {
        try {
            checkReleaseDate(film);
            film.setId(getNextId(films));
            log.info("Добавлен фильм с id: {}", film.getId());
            films.put(film.getId(), film);
            return ResponseEntity.status(HttpStatus.CREATED).body(film);
        } catch (ValidationException e) {
            log.error("Произошла ошибка валидации: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(film);
        }
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {
        if (film.getId() == null) {
            log.error("Не указан id фильма");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(film);
        } else if (!films.containsKey(film.getId())) {
            log.error("Фильм с id: {} не найден", film.getId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(film);
        }
        try {
            checkReleaseDate(film);
            log.info("Изменен фильм с id: {}", film.getId());
            films.put(film.getId(), film);
            return ResponseEntity.status(HttpStatus.OK).body(film);
        } catch (ValidationException e) {
            log.error("Произошла ошибка валидации: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    private void checkReleaseDate(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Неверная дата релиза");
        }
    }
}