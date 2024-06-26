package ru.yandex.practicum.filmorate.repository.genre;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.BaseRepository;
import ru.yandex.practicum.filmorate.request.GenreTextRequests;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@Primary
public class GenreDBRepository extends BaseRepository<Genre> implements GenreRepository {
    public GenreDBRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<Genre> getAllGenres() {
        return findMany(GenreTextRequests.FIND_ALL);
    }

    @Override
    public Optional<Genre> getGenreById(int genreId) {
        return findOne(GenreTextRequests.FIND_BY_ID, genreId);
    }

    @Override
    public Set<Integer> getGenresByFilm(Integer filmId) {
        return findMany(GenreTextRequests.FIND_GENRE_BY_FILM_ID, filmId).stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());
    }
}