package ru.yandex.practicum.filmorate.controller;

import java.util.Map;

public abstract class BaseController<T> {
    protected long getNextId(Map<Long, T> map) {
        long currentMaxId = map.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}