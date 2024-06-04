package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

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
        setName(user);
        log.info("Добавлен пользователь с id: {}", user.getId());
        return userStorage.create(user);
    }

    public User update(User user) {
        checkUserExists(user.getId());
        setName(user);
        log.info("Обновлены данные пользователя с id: {}", user.getId());
        return userStorage.update(user);
    }

    public void addFriend(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        userStorage.addFriend(user, friend);
        log.info("Пользователь с id: {} добавил в друзья пользователя с id: {}", userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        userStorage.removeFriend(user, friend);
        log.info("Пользователь с id: {} удалил из друзей пользователя с id: {}", userId, friendId);
    }

    public Collection<User> getFriends(Long userId) {
        User user = getUserById(userId);
        log.info("Возврат списка друзей пользователя с id: {}", userId);
        return userStorage.getFriends(user);
    }

    public Collection<User> getCommonFriends(Long userId, Long otherUserId) {
        User user = getUserById(userId);
        User otherUser = getUserById(otherUserId);
        log.info("Возврат списка общих друзей пользователей с id: {} и {}", userId, otherUserId);
        return userStorage.getCommonFriends(user, otherUser);
    }

    private void setName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("Логин будет использован, как имя пользователя: {}", user.getLogin());
            user.setName(user.getLogin());
        }
    }

    private void checkUserExists(Long userId) {
        if (!userStorage.getUserById(userId).isPresent()) {
            log.error("Пользователь с id: {} не найден", userId);
            throw new NotFoundException("Пользователь с id: " + userId + " не найден");
        }
    }
}