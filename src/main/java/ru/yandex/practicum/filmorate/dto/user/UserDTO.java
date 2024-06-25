package ru.yandex.practicum.filmorate.dto.user;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class UserDTO {
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}
