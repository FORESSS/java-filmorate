package ru.yandex.practicum.filmorate.repository.user;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.BaseRepository;

import java.util.Collection;
import java.util.List;

@Repository
@Primary
public class UserDBRepository extends BaseRepository<User> implements UserRepository {
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id=?";
    private static final String FIND_BY_PARAM_QUERY = "SELECT * FROM users WHERE email = ? AND login=? AND birthday=?";
    private static final String INSERT_QUERY =
            "INSERT INTO users(email, login, username, birthday) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY =
            "UPDATE users SET email = ?, login = ?, username = ?, birthday = ? WHERE id = ?";
    private static final String GET_ALL_FRIENDS_QUERY =
            "SELECT id, email,login, username, birthday FROM users JOIN friends ON id=friend_id WHERE user_id=?";

    private static final String GET_MUTUAL_FRIENDS_QUERY =
            "SELECT id, email,login, username, birthday\n" +
                    "FROM users JOIN friends ON id=friend_id\n" +
                    "WHERE user_id=?\n" +
                    "INTERSECT\n" +
                    "SELECT id, email,login, username, birthday\n" +
                    "FROM users JOIN friends ON id=friend_id\n" +
                    "WHERE user_id=?";

    public UserDBRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<User> getAllUsers() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public boolean containsUserById(int userId) {
        return findOne(FIND_BY_ID_QUERY, userId).isPresent();
    }

    @Override
    public boolean containsUserByValue(User user) {
        List<User> sameUserList = findMany(FIND_BY_PARAM_QUERY, user.getEmail(), user.getLogin(), user.getBirthday());
        return !sameUserList.isEmpty();
    }

    @Override
    public User addUser(User user) {
        int id = insert(
                INSERT_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
        user.setId(id);
        return user;
    }

    @Override
    public User updateUser(User user) {
        update(
                UPDATE_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        return user;
    }

    @Override
    public Collection<User> getAllFriends(Integer userId) {
        return findMany(GET_ALL_FRIENDS_QUERY, userId);
    }

    @Override
    public Collection<User> getMutualFriends(Integer userId, Integer otherId) {
        return findMany(GET_MUTUAL_FRIENDS_QUERY, userId, otherId);
    }
}
