package ru.yandex.practicum.filmorate.request;

public class GenreTextRequests {
    public static final String FIND_ALL = "SELECT * FROM genres";
    public static final String FIND_BY_ID = "SELECT * FROM genres WHERE id = ?";
    public static final String FIND_GENRE_BY_FILM_ID =
            "SELECT gr.id, gr.name FROM film_genres AS fg JOIN genres gr ON gr.id=fg.genre_id WHERE film_id=?";
}