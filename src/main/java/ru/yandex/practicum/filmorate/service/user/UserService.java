package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public Collection<User> getAllUsers() {
        log.info("Возврат списка пользователей");
        return userStorage.getAllUsers();
    }

    public User getUserById(Long userId) {
        log.info("Возврат пользователя с id: {}", userId);
        return userStorage.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не найден"));
    }

    public User create(User user) {
        log.info("Добавлен пользователь с id: {}", user.getId());
        return userStorage.create(user);
    }

    public User update(User newUser) {
        log.info("Обновлены данные пользователя с id: {}", newUser.getId());
        return userStorage.update(newUser);
    }

    public void addFriend(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        userStorage.addFriend(user, friend);
        log.info("Пользователь с id: {} добавил в друзья пользователя с id: {}", user.getId(), friend.getId());

    }

    public void removeFriend(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        userStorage.removeFriend(user, friend);
        log.info("Пользователь с id: {} удалил из друзей пользователя с id: {}", user.getId(), friend.getId());
    }

    public List<User> getFriends(Long userId) {
        log.info("Возврат списка друзей пользователя с id: {}", userId);
        return userStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(Long userId, Long otherUserId) {
        User user = getUserById(userId);
        User otherUser = getUserById(otherUserId);
        log.info("Возврат списка общих друзей пользователей с id: {} и {}", user.getId(), otherUser.getId());
        return userStorage.getCommonFriends(user, otherUser);
    }
}