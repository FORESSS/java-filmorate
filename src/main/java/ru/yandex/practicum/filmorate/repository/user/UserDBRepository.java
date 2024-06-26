package ru.yandex.practicum.filmorate.repository.user;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.BaseRepository;
import ru.yandex.practicum.filmorate.request.UserTextRequests;

import java.util.Collection;
import java.util.List;

@Repository
@Primary
public class UserDBRepository extends BaseRepository<User> implements UserRepository {
    public UserDBRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<User> getAllUsers() {
        return findMany(UserTextRequests.FIND_ALL);
    }

    @Override
    public User getUserById(int userId) {
        return findOne(UserTextRequests.GET_USER_BY_ID, userId).orElseThrow(() ->
                new IdNotFoundException("Пользователь с id: " + userId + " не найден"));
    }

    @Override
    public User addUser(User user) {
        int id = insert(
                UserTextRequests.INSERT,
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
                UserTextRequests.UPDATE,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        return user;
    }

    @Override
    public boolean containsUserById(int userId) {
        return findOne(UserTextRequests.FIND_BY_ID, userId).isPresent();
    }

    @Override
    public boolean containsUserByValue(User user) {
        List<User> sameUserList = findMany(UserTextRequests.FIND_BY_PARAM, user.getEmail(), user.getLogin(), user.getBirthday());
        return !sameUserList.isEmpty();
    }

    @Override
    public Collection<User> getAllFriends(Integer userId) {
        return findMany(UserTextRequests.GET_ALL_FRIENDS, userId);
    }

    @Override
    public Collection<User> getMutualFriends(Integer userId, Integer otherId) {
        return findMany(UserTextRequests.GET_MUTUAL_FRIENDS, userId, otherId);
    }
}