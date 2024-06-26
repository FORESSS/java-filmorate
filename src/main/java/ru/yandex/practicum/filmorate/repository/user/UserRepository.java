package ru.yandex.practicum.filmorate.repository.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserRepository {
    Collection<User> getAllUsers();

    User getUserById(int userId);

    User addUser(User user);

    User updateUser(User user);

    boolean containsUserById(int id);

    boolean containsUserByValue(User user);

    Collection<User> getAllFriends(Integer userId);

    Collection<User> getMutualFriends(Integer userId, Integer otherId);
}