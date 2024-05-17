package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
        try {
            checkEmail(user);
            checkLogin(user);
            setName(user);
            user.setId(getNextId(users));
            log.info("Добавлен пользователь с id: {}", user.getId());
            users.put(user.getId(), user);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (ValidationException e) {
            log.error("Произошла ошибка валидации: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(user);
        }
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
        try {
            checkEmail(user);
            checkLogin(user);
            setName(user);
            log.info("Обновлены данные пользователя: {}", user.getId());
            users.put(user.getId(), user);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (ValidationException e) {
            log.error("Произошла ошибка валидации: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(user);
        }
    }

    private void setName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("Логин будет использован, как имя пользователя: {}", user.getLogin());
            user.setName(user.getLogin());
        }
    }

    private void checkEmail(User user) {
        if (users.values().stream().anyMatch(us -> us.getEmail().equals(user.getEmail()))) {
            log.error("Email {} уже используется", user.getEmail());
            throw new ValidationException("Email: " + user.getEmail() + " уже используется");
        }
    }

    private void checkLogin(User user) {
        if (users.values().stream().anyMatch(us -> us.getLogin().equals(user.getLogin()))) {
            log.error("Логин {} уже занят", user.getLogin());
            throw new ValidationException("Логин: " + user.getLogin() + " уже занят");
        }
    }
}