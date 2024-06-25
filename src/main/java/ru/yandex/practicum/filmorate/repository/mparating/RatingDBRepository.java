package ru.yandex.practicum.filmorate.repository.mparating;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.repository.BaseRepository;

import java.util.Collection;
import java.util.Optional;

@Repository
@Primary
public class RatingDBRepository extends BaseRepository<Rating> implements RatingRepository {
    private static final String FIND_ALL_QUERY = "SELECT * FROM mpa_rating";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM mpa_rating WHERE id = ?";

    public RatingDBRepository(JdbcTemplate jdbc, RowMapper<Rating> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<Rating> getAllRatings() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Optional<Rating> getRatingById(int ratingId) {
        return findOne(FIND_BY_ID_QUERY, ratingId);
    }
}
