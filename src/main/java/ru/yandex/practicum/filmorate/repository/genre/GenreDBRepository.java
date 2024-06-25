package ru.yandex.practicum.filmorate.repository.genre;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.BaseRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@Primary
public class GenreDBRepository extends BaseRepository<Genre> implements GenreRepository {
    private static final String FIND_ALL_QUERY = "SELECT * FROM genre";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genre WHERE id = ?";
    private static final String FIND_GENRE_BY_FILM_ID_QUERY =
            "SELECT gr.id, gr.name FROM film_genre AS fg JOIN genre gr ON gr.id=fg.genre_id WHERE film_id=?";

    public GenreDBRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<Genre> getAllGenres() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Optional<Genre> getGenreById(int genreId) {
        return findOne(FIND_BY_ID_QUERY, genreId);
    }

    @Override
    public Set<Integer> getGenresByFilm(Integer filmId) {
        return findMany(FIND_GENRE_BY_FILM_ID_QUERY, filmId).stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());
    }
}
