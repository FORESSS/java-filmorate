package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseStorage;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage extends BaseStorage<User> implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public User create(User user) {
        long newId = getNextId(users);
        user.setId(newId);
        users.put(newId, user);
        return user;
    }

    @Override
    public User update(User newUser) {
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public void addFriend(User user, User friend) {
        user.getFriends().add(friend.getId());
        friend.getFriends().add(user.getId());
    }

    @Override
    public void removeFriend(User user, User friend) {
        user.getFriends().remove(friend.getId());
        friend.getFriends().remove(user.getId());
    }

    @Override
    public List<User> getFriends(User user) {
        return user.getFriends().stream()
                .map(this::getUserById)
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getCommonFriends(User user, User otherUser) {
        Set<Long> commonFriendIds = user.getFriends().stream()
                .filter(otherUser.getFriends()::contains)
                .collect(Collectors.toSet());
        return commonFriendIds.stream()
                .map(this::getUserById)
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }
}