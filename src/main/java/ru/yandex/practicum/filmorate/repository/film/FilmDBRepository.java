package ru.yandex.practicum.filmorate.repository.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.BaseRepository;
import ru.yandex.practicum.filmorate.repository.genre.GenreRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class FilmDBRepository extends BaseRepository<Film> implements FilmRepository {
    private static final String INSERT_QUERY =
            "INSERT INTO film(name, description, release_date, duration, mpa_rating_id) VALUES (?,?,?,?,?)";
    private static final String FIND_BY_PARAM_QUERY =
            "SELECT * FROM film WHERE name=? AND release_date=? AND duration=?";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM film WHERE id=?";
    private static final String UPDATE_QUERY =
            "UPDATE film SET name=?, description=?, release_date=?, duration=?, mpa_rating_id=? WHERE id = ?";
    private static final String DELETE_GENRES_QUERY = "DELETE FROM film_genre WHERE film_id=?";
    private static final String INSERT_GENRES_QUERY = "INSERT INTO film_genre(film_id,genre_id) VALUES (?,?)";
    private static final String GET_ALL_FILMS_QUERY = "SELECT * FROM film";
    private static final String ADD_USERLIKE_TOFILM_QUERY = "INSERT INTO likes(film_id,user_id) VALUES (?,?);";
    private static final String GET_USERLIKE_TOFILM_QUERY = "SELECT COUNT(*) FROM likes WHERE film_id=? AND user_id=?";
    private static final String DELETE_USERLIKE_TOFILM_QUERY = "DELETE FROM likes WHERE film_id=? AND user_id=?";
    private static final String GET_FILM_LIKES_QUERY = "SELECT COUNT(*) FROM likes WHERE film_id=?";
//        private static final String GET_MOST_POPULAR_FILMS_QUERY =
//            "SELECT f.id,f.name,f.description, f.release_date, f.duration, f.mpa_rating_id " +
//                    "FROM likes AS l LEFT JOIN film AS f ON f.id=l.film_id " +
//                    "GROUP BY  f.id,f.name,f.description, f.release_date, f.duration, f.mpa_rating_id " +
//                    "ORDER BY COUNT(f.id) DESC LIMIT ?;";
    private static final String GET_MOST_POPULAR_FILMS_QUERY =
            "SELECT f.id,f.name,f.description, f.release_date, f.duration, f.mpa_rating_id FROM film AS f LEFT JOIN" +
                    "(SELECT film_id, COUNT(*) AS count FROM likes GROUP BY film_id) AS t ON f.id=t.film_id " +
                    "ORDER BY count DESC LIMIT ?;";

    private static final String GET_FILM_BY_ID = "SELECT * FROM film WHERE id=?";


    private final GenreRepository genreRepository;


    public FilmDBRepository(JdbcTemplate jdbc, RowMapper<Film> mapper, GenreRepository genreRepository) {
        super(jdbc, mapper);
        this.genreRepository = genreRepository;
    }

    @Override
    public Film addFilm(Film film) {
        int filmId = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpaId()
        );
        film.setId(filmId);
        film.getGenresId().forEach(genreId -> insertMultKeys(INSERT_GENRES_QUERY, filmId, genreId));
        return film;
    }

    @Override
    public boolean containsFilmByValue(Film film) {
        List<Film> sameFilmList = findMany(FIND_BY_PARAM_QUERY, film.getName(), film.getReleaseDate(), film.getDuration());
        return !sameFilmList.isEmpty();
    }

    @Override
    public Film updateFilm(Film film) {
        int filmId = film.getId();
        update(
                UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpaId(),
                filmId
        );
        delete(DELETE_GENRES_QUERY, filmId);
        film.getGenresId().forEach(genreId -> insertMultKeys(INSERT_GENRES_QUERY, filmId, genreId));
        return film;
    }

    @Override
    public boolean containsFilmById(int filmId) {
        return findOne(FIND_BY_ID_QUERY, filmId).isPresent();
    }

    @Override
    public Collection<Film> getAllFilms() {
        Collection<Film> films = findMany(GET_ALL_FILMS_QUERY);
        for (Film film : films) {
            int filmId = film.getId();
            Set<Integer> genresId = genreRepository.getGenresByFilm(filmId);
            film.setGenresId(genresId);
        }
        return films;
    }

    @Override
    public void addUserLikeToFilm(Integer filmId, Integer userId) {
        insertMultKeys(ADD_USERLIKE_TOFILM_QUERY, filmId, userId);
    }

    @Override
    public boolean containsUserLikeForFilm(Integer filmId, Integer userId) {
        var likes = Optional.ofNullable(jdbc.queryForObject(GET_USERLIKE_TOFILM_QUERY, Integer.class, filmId, userId));
        return likes.isPresent() && !likes.get().equals(0);
    }

    @Override
    public void deleteUserLikeFromFilm(Integer filmId, Integer userId) {
        delete(DELETE_USERLIKE_TOFILM_QUERY, filmId, userId);
    }

    @Override
    public int getFilmLikes(Integer filmId) {
        return Optional
                .ofNullable(jdbc.queryForObject(GET_FILM_LIKES_QUERY, Integer.class, filmId))
                .orElse(0);
    }

    @Override
    public Collection<Film> getMostPopularFilms(int count) {
        List<Film> films = findMany(GET_MOST_POPULAR_FILMS_QUERY, count);
        for (Film film : films) {
            int filmId = film.getId();
            Set<Integer> genresId = genreRepository.getGenresByFilm(filmId);
            film.setGenresId(genresId);
        }
        return films;
    }

    @Override
    public Film getFilmById(int filmId) {
        Film film = findOne(GET_FILM_BY_ID, filmId).orElseThrow();
        film.setGenresId(genreRepository.getGenresByFilm(filmId));
        return film;
    }
}
