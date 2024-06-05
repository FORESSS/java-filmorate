package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

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
        log.info("Добавлен фильм с id: {}", film.getId());
        return filmStorage.create(film);
    }

    public Film update(Film newFilm) {
        log.info("Изменен фильм с id: {}", newFilm.getId());
        return filmStorage.update(newFilm);
    }

    public void like(Long filmId, Long userId) {
        Film film = getFilmById(filmId);
        User user = userService.getUserById(userId);
        filmStorage.like(film, user.getId());
        log.info("Пользователь с id: {} поставил лайк фильму с id: {}", userId, film.getId());
    }

    public void removeLike(Long filmId, Long userId) {
        Film film = getFilmById(filmId);
        User user = userService.getUserById(userId);
        filmStorage.removeLike(film, user.getId());
        log.info("Пользователь с id: {} удалил лайк у фильма с id: {}", userId, film.getId());
    }

    public List<Film> getPopular(int count) {
        log.info("Возврат списка популярных фильмов");
        return filmStorage.getPopular(count);
    }
}