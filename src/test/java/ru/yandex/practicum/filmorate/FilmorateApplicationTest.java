package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.GenreController;
import ru.yandex.practicum.filmorate.controller.RatingController;
import ru.yandex.practicum.filmorate.controller.UserController;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class FilmorateApplicationTest {
    @Autowired
    private FilmController filmController;
    @Autowired
    private UserController userController;
    @Autowired
    private GenreController genreController;
    @Autowired
    private RatingController ratingController;

    @Test
    void contextLoadsTest() {
        assertThat(filmController).isNotNull();
        assertThat(userController).isNotNull();
        assertThat(genreController).isNotNull();
        assertThat(ratingController).isNotNull();
    }
}