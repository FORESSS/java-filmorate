package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
        setName(user);
        user.setId(getNextId(users));
        log.info("Добавлен пользователь с id: {}", user.getId());
        users.put(user.getId(), user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        if (user.getId() == null) {
            log.error("Не указан id пользователя");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(user);
        } else if (!users.containsKey(user.getId())) {
            log.error("Пользователь с id: {} не найден", user.getId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(user);
        }
        setName(user);
        log.info("Обновлены данные пользователя: {}", user.getId());
        users.put(user.getId(), user);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    private void setName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("Логин будет использован, как имя пользователя: {}", user.getLogin());
            user.setName(user.getLogin());
        }
    }
}