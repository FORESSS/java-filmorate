package ru.yandex.practicum.filmorate.storage;

import java.util.Map;

public abstract class BaseStorage<T> {
    protected long getNextId(Map<Long, T> map) {
        return map.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0) + 1;
    }
}