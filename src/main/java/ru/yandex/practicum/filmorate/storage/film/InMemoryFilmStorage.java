package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.BaseStorage;

@Component
public class InMemoryFilmStorage extends BaseStorage<Film> implements FilmStorage {

}