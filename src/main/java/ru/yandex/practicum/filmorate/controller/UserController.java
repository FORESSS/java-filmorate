package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController extends BaseController<User> {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Возврат списка пользователей");
        return users.values();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        checkEmailAndLogin(user);
        checkName(user);
        user.setId(getNextId(users));
        log.info("Добавлен пользователь с id: {}", user.getId());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        checkId(user);
        checkEmailAndLogin(user);
        checkName(user);
        log.info("Изменены данные пользователя с id: {}", user.getId());
        users.put(user.getId(), user);
        return user;
    }

    private void checkId(User user) {
        if (user.getId() == null) {
            log.error("Не указан id");
            throw new ValidationException("Не указан id");
        } else if (!users.containsKey(user.getId())) {
            log.error("Пользователь с id: {} не найден", user.getId());
            throw new ValidationException("Пользователь с id: " + user.getId() + " не найден");
        }
    }

    private void checkName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("В качестве имени будет использован логин {}", user.getLogin());
            user.setName(user.getLogin());
        }
    }

    private void checkEmailAndLogin(User user) {
        if (users.values().stream().anyMatch(us -> us.getEmail().equals(user.getEmail()))) {
            log.error("E-mail {} занят", user.getEmail());
            throw new ValidationException("E-mail: " + user.getEmail() + " занят");
        }
        if (users.values().stream().anyMatch(us -> us.getLogin().equals(user.getLogin()))) {
            log.error("Логин {} занят", user.getLogin());
            throw new ValidationException("Логин: " + user.getLogin() + " занят");
        }
    }
}