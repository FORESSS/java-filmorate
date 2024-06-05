package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseStorage;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage extends BaseStorage<User> implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private final Map<Long, Set<User>> friends = new HashMap<>();

    @Override
    public Collection<User> getAllUsers() {
        log.info("Возврат списка пользователей");
        return users.values();
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        checkId(userId);
        log.info("Возврат пользователя с id: {}", userId);
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public User create(User user) {
        setName(user);
        long newId = getNextId(users);
        user.setId(newId);
        users.put(newId, user);
        log.info("Добавлен пользователь с id: {}", user.getId());
        return user;
    }

    @Override
    public User update(User newUser) {
        checkId(newUser.getId());
        checkUserExists(newUser.getId());
        setName(newUser);
        users.put(newUser.getId(), newUser);
        log.info("Обновлены данные пользователя с id: {}", newUser.getId());
        return newUser;
    }

    @Override
    public void addFriend(User user, User friend) {
        Set<User> userFriends = getFriendSet(user);
        userFriends.add(friend);
        Set<User> friendFriends = getFriendSet(friend);
        friendFriends.add(user);
        log.info("Пользователь с id: {} добавил в друзья пользователя с id: {}", user.getId(), friend.getId());
    }

    @Override
    public void removeFriend(User user, User friend) {
        Set<User> userFriends = getFriendSet(user);
        userFriends.remove(friend);
        Set<User> friendFriends = getFriendSet(friend);
        friendFriends.remove(user);
        log.info("Пользователь с id: {} удалил из друзей пользователя с id: {}", user.getId(), friend.getId());
    }

    @Override
    public List<User> getFriends(Long userId) {
        checkId(userId);
        Set<User> userFriends = getFriendSet(getUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не найден")));
        log.info("Возврат списка друзей пользователя с id: {}", userId);
        return new ArrayList<>(userFriends);
    }

    @Override
    public List<User> getCommonFriends(User user, User otherUser) {
        Set<User> userFriends = getFriendSet(user);
        Set<User> otherUserFriends = getFriendSet(otherUser);
        userFriends.retainAll(otherUserFriends);
        log.info("Возврат списка общих друзей пользователей с id: {} и {}", user.getId(), otherUser.getId());
        return userFriends.stream().toList();
    }

    private void setName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("Логин будет использован, как имя пользователя: {}", user.getLogin());
            user.setName(user.getLogin());
        }
    }

    private void checkUserExists(Long userId) {
        if (!getUserById(userId).isPresent()) {
            log.error("Пользователь с id: {} не найден", userId);
            throw new NotFoundException("Пользователь с id: " + userId + " не найден");
        }
    }

    private Set<User> getFriendSet(User user) {
        return friends.computeIfAbsent(user.getId(), (k) -> new HashSet<>());
    }
}