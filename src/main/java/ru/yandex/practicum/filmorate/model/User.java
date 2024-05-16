package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {
    private Long id;

    @NotBlank(message = "Поле e-mail не может быть пустым")
    @Email(message = "Должен быть корректный e-mail")
    private String email;

    @NotBlank(message = "Поле login не может быть пустым")
    @Pattern(regexp = "^\\S+$", message = "Логин не может содержать пробелов")
    private String login;

    private String name;

    @NotNull(message = "Обязательное поле")
    @Past(message = "Дата рождения должна быть меньше текущей даты")
    private LocalDate birthday;
}