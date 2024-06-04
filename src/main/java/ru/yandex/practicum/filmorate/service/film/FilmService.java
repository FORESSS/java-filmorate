package ru.yandex.practicum.filmorate.service.film;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;
    private static final LocalDate EARLIEST_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    public Collection<Film> getAllFilms() {
        log.info("Возврат списка фильмов");
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(Long filmId) {
        log.info("Возврат фильма с id: {}", filmId);
        return filmStorage.getFilmById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с id: " + filmId + " не найден"));
    }

    public Film create(Film film) {
        validateFilm(film);
        log.info("Добавлен фильм с id: {}", film.getId());
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        validateFilm(film);
        checkFilmExists(film.getId());
        log.info("Изменен фильм с id: {}", film.getId());
        return filmStorage.update(film);
    }

    public void like(Long filmId, Long userId) {
        Film film = getFilmById(filmId);
        User user = userService.getUserById(userId);
        filmStorage.like(film, user.getId());
        log.info("Пользователь с id: {} поставил лайк фильму с id: {}", user.getId(), filmId);
    }

    public void removeLike(Long filmId, Long userId) {
        Film film = getFilmById(filmId);
        User user = userService.getUserById(userId);
        filmStorage.removeLike(film, user.getId());
        log.info("Пользователь с id: {} убрал лайк у фильма с id: {}", user.getId(), filmId);
    }

    public List<Film> getPopular(int count) {
        log.info("Возврат списка популярных фильмов");
        return filmStorage.getPopular(count);
    }

    private void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(EARLIEST_RELEASE_DATE)) {
            throw new ValidationException("Неверная дата релиза");
        }
    }

    private void checkFilmExists(Long filmId) {
        if (!filmStorage.getFilmById(filmId).isPresent()) {
            log.error("Фильм с id: {} не найден", filmId);
            throw new NotFoundException("Фильм с id: " + filmId + " не найден");
        }
    }
}