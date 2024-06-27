package ru.yandex.practicum.filmorate.request;

public class RatingTextRequests {
    public static final String FIND_ALL = "SELECT * FROM ratings";
    public static final String FIND_BY_ID = "SELECT * FROM ratings WHERE id = ?";
}