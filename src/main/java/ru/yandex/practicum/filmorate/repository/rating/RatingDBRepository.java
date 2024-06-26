package ru.yandex.practicum.filmorate.repository.rating;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.repository.BaseRepository;
import ru.yandex.practicum.filmorate.request.RatingTextRequests;

import java.util.Collection;
import java.util.Optional;

@Repository
@Primary
public class RatingDBRepository extends BaseRepository<Rating> implements RatingRepository {
    public RatingDBRepository(JdbcTemplate jdbc, RowMapper<Rating> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<Rating> getAllRatings() {
        return findMany(RatingTextRequests.FIND_ALL);
    }

    @Override
    public Optional<Rating> getRatingById(int ratingId) {
        return findOne(RatingTextRequests.FIND_BY_ID, ratingId);
    }
}