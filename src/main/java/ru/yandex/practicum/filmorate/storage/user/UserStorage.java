package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserStorage {
    Collection<User> getAllUsers();

    Optional<User> getUserById(Long userId);

    User create(User user);

    User update(User newUser);

    void addFriend(User user, User friend);

    void removeFriend(User user, User friend);

    List<User> getFriends(Long userId);

    List<User> getCommonFriends(User user, User otherUser);
}