package ru.yandex.practicum.filmorate.dto.user;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class NewUserRequestDTO {
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