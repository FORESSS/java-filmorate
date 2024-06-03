package ru.yandex.practicum.filmorate.controller.error;

import lombok.Data;

@Data
public class ErrorResponse {
    private final String error;
    private final String description;
}