package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.film.FilmDTO;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequestDTO;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequestDTO;
import ru.yandex.practicum.filmorate.dto.genre.GenreRequestDTO;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.film.FilmRepository;
import ru.yandex.practicum.filmorate.repository.genre.GenreRepository;
import ru.yandex.practicum.filmorate.repository.mparating.RatingRepository;
import ru.yandex.practicum.filmorate.utils.FilmMapper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmRepository filmRepository;
    private final GenreRepository genreRepository;
    private final RatingRepository ratingRepository;
    private final UserService userService;
    private final FilmMapper filmMapper;

    public Collection<FilmDTO> getAllFilms() {
        log.info("Возврат списка фильмов");
        return filmRepository.getAllFilms()
                .stream()
                .map(filmMapper::filmToDTO)
                .toList();
    }

    public FilmDTO getFilmById(int filmId) {
        checkFilmId(filmId);
        log.info("Возврат фильма с id: {}", filmId);
        return filmMapper.filmToDTO(filmRepository.getFilmById(filmId));
    }

    public FilmDTO addFilm(NewFilmRequestDTO dto) {
        checkRatingId(dto.getMpa().getId());
        checkGenreId(dto.getGenres());
        Film film = FilmMapper.newFilmRequestToFilm(dto);
        Film newFilm = filmRepository.addFilm(film);
        log.info("Добавлен фильм с id: {}", newFilm.getId());
        return filmMapper.filmToDTO(film);
    }

    public FilmDTO updateFilm(UpdateFilmRequestDTO dto) {
        checkFilmId(dto.getId());
        checkRatingId(dto.getMpa().getId());
        checkGenreId(dto.getGenres());
        Film film = FilmMapper.updateFilmRequestDTOToFilm(dto);
        filmRepository.updateFilm(film);
        log.info("Изменен фильм с id: {}", film.getId());
        return filmMapper.filmToDTO(film);
    }

    public void addLikeToFilm(Integer filmId, Integer userId) {
        checkFilmId(filmId);
        userService.checkUserId(userId);
        if (!filmRepository.containsUserLikeForFilm(filmId, userId)) {
            filmRepository.addUserLikeToFilm(filmId, userId);
            log.info("Пользователь с id: {} поставил лайк фильму с id: {}", userId, filmId);
        }
    }

    public void deleteLikeFromFilm(Integer filmId, Integer userId) {
        checkFilmId(filmId);
        userService.checkUserId(userId);
        if (filmRepository.containsUserLikeForFilm(filmId, userId)) {
            filmRepository.deleteUserLikeFromFilm(filmId, userId);
            log.info("Пользователь с id: {} удалил лайк у фильма с id: {}", userId, filmId);
        }
    }

    public Collection<FilmDTO> getMostPopularFilms(Integer count) {

        log.info("Возврат списка популярных фильмов");
        return filmRepository.getMostPopularFilms(count)
                .stream()
                .map(filmMapper::filmToDTO)
                .toList();
    }

    public int getFilmLikes(Integer filmId) {
        checkFilmId(filmId);
        log.info("Возврат количества лайков у фильма с id: {}", filmId);
        return filmRepository.getFilmLikes(filmId);
    }

    private void checkFilmId(Integer filmId) {
        if (!filmRepository.containsFilmById(filmId)) {
            throw new IdNotFoundException("Фильм с id: " + filmId + " не найден");
        }
    }

    private void checkRatingId(Integer mpaId) {
        if (ratingRepository.getRatingById(mpaId).isEmpty()) {
            throw new IllegalArgumentException("Рейтинг с id: " + mpaId + " не найден");
        }
    }

    private void checkGenreId(List<GenreRequestDTO> genresDTOList) {
        if (genresDTOList == null) {
            return;
        }
        boolean isAllGenreIdPresent = genresDTOList
                .stream()
                .map(GenreRequestDTO::getId)
                .map(genreRepository::getGenreById)
                .allMatch(Optional::isPresent);
        if (!isAllGenreIdPresent) {
            throw new IllegalArgumentException("Жанр не найден");
        }
    }
}