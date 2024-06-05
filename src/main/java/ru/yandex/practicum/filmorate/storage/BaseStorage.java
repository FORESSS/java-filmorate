package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.InvalidRequestException;

import java.util.Map;

@Slf4j
public abstract class BaseStorage<T> {
    protected long getNextId(Map<Long, T> map) {
        return map.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0) + 1;
    }

    protected void checkId(Long id) {
        if (id == null || id <= 0) {
            log.error("Указан неккоректный id");
            throw new InvalidRequestException("Указан неккоректный id");
        }
    }
}