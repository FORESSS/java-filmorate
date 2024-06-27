package ru.yandex.practicum.filmorate.repository.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.BaseRepository;
import ru.yandex.practicum.filmorate.repository.genre.GenreRepository;
import ru.yandex.practicum.filmorate.request.FilmTextRequests;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class FilmDBRepository extends BaseRepository<Film> implements FilmRepository {
    private final GenreRepository genreRepository;

    public FilmDBRepository(JdbcTemplate jdbc, RowMapper<Film> mapper, GenreRepository genreRepository) {
        super(jdbc, mapper);
        this.genreRepository = genreRepository;
    }

    @Override
    public Collection<Film> getAllFilms() {
        Collection<Film> films = findMany(FilmTextRequests.GET_ALL_FILMS);
        for (Film film : films) {
            int filmId = film.getId();
            Set<Integer> genresId = genreRepository.getGenresByFilm(filmId);
            film.setGenresId(genresId);
        }
        return films;
    }

    @Override
    public Film getFilmById(int filmId) {
        Film film = findOne(FilmTextRequests.GET_FILM_BY_ID, filmId).orElseThrow();
        film.setGenresId(genreRepository.getGenresByFilm(filmId));
        return film;
    }

    @Override
    public Film addFilm(Film film) {
        int filmId = insert(
                FilmTextRequests.INSERT,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpaId()
        );
        film.setId(filmId);
        film.getGenresId().forEach(genreId -> insertMultKeys(FilmTextRequests.INSERT_GENRES, filmId, genreId));
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        int filmId = film.getId();
        update(
                FilmTextRequests.UPDATE,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpaId(),
                filmId
        );
        delete(FilmTextRequests.DELETE_GENRES, filmId);
        film.getGenresId().forEach(genreId -> insertMultKeys(FilmTextRequests.INSERT_GENRES, filmId, genreId));
        return film;
    }

    @Override
    public void addUserLikeToFilm(Integer filmId, Integer userId) {
        insertMultKeys(FilmTextRequests.ADD_USERLIKE_TO_FILM, filmId, userId);
    }

    @Override
    public void deleteUserLikeFromFilm(Integer filmId, Integer userId) {
        delete(FilmTextRequests.DELETE_USERLIKE_TO_FILM, filmId, userId);
    }

    @Override
    public boolean containsFilmById(int filmId) {
        return findOne(FilmTextRequests.GET_FILM_BY_ID, filmId).isPresent();
    }

    @Override
    public boolean containsFilmByValue(Film film) {
        Boolean exists = jdbc.queryForObject(
                FilmTextRequests.CONTAINS_FILM_BY_VALUE,
                Boolean.class,
                film.getName(),
                film.getReleaseDate(),
                film.getDuration()
        );
        return exists != null && exists;
    }

    @Override
    public boolean containsUserLikeForFilm(Integer filmId, Integer userId) {
        Integer likes = jdbc.queryForObject(FilmTextRequests.GET_USERLIKE_TO_FILM, Integer.class, filmId, userId);
        return likes != null && likes > 0;
    }

    @Override
    public int getFilmLikes(Integer filmId) {
        return Optional
                .ofNullable(jdbc.queryForObject(FilmTextRequests.GET_FILM_LIKES, Integer.class, filmId))
                .orElse(0);
    }

    @Override
    public Collection<Film> getMostPopularFilms(int count) {
        List<Film> films = findMany(FilmTextRequests.GET_MOST_POPULAR_FILMS, count);
        for (Film film : films) {
            int filmId = film.getId();
            Set<Integer> genresId = genreRepository.getGenresByFilm(filmId);
            film.setGenresId(genresId);
        }
        return films;
    }
}