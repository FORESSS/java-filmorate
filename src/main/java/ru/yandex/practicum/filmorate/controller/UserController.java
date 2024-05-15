package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import java.time.Instant;
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
        log.info("Возврат списка пользователей на эндпоинт GET /users");
        return users.values();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        checkUser(user);
        if (user.getName() == null) {
            setUserName(user);
        }
        user.setId(getNextId(users));
        log.info("Добавлен пользователь с id={}", user.getId());
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @PutMapping
    public User updateUser(@Valid  @RequestBody User user) {
        checkUser(user);
        if (user.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        } else if (!users.containsKey(user.getId())) {
            throw new ValidationException("user c id=" + user.getId() + " не найден");
        }
        if (user.getName() == null) {
            setUserName(user);
        }
        log.info("Изменен пользователь с id={}", user.getId());
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    private void checkUser(User user) {
        if (user == null) {
            throw new ValidationException("user equal null");
        } else if (Instant.parse(user.getBirthday() + "T00:00:00Z").isAfter(Instant.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }

    private void setUserName(User user) {
        user.setName(user.getLogin());
    }
}