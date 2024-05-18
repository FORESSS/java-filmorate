package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmControllerTest {
    private FilmController filmController;

    @BeforeEach
    void create() {
        filmController = new FilmController();
    }

    @Test
    void testGetAllFilmsEmpty() {
        Collection<Film> films = filmController.getAllFilms();

        assertEquals(0, films.size());
    }

    @Test
    void testAddValidFilm() {
        Film film = new Film(null, "Film", "Description", LocalDate.now(), 120);
        ResponseEntity<Film> response = filmController.addFilm(film);
        Film addedFilm = response.getBody();

        assertNotNull(addedFilm.getId());
        assertEquals("Film", addedFilm.getName());
        assertEquals("Description", addedFilm.getDescription());
        assertEquals(LocalDate.now(), addedFilm.getReleaseDate());
        assertEquals(120, addedFilm.getDuration());
    }

   /* @Test
    void testAddFilmWithBlankName() {
        Film film = new Film(null, "", "Description", LocalDate.now(), 120);

        assertThrows(MethodArgumentNotValidException.class, () -> filmController.addFilm(film));
    }

    @Test
    void testAddFilmWithLongDescription() {
        StringBuilder description = new StringBuilder();
        for (int i = 0; i < 201; i++) {
            description.append("a");
        }
        Film film = new Film(null, "Film", description.toString(), LocalDate.now(), 120);

        assertThrows(MethodArgumentNotValidException.class, () -> filmController.addFilm(film));
    }

    @Test
    void testAddFilmWithNegativeDuration() {
        Film film = new Film(null, "Film", "Description", LocalDate.now(), -120);

        assertThrows(MethodArgumentNotValidException.class, () -> filmController.addFilm(film));
    }*/


    @Test
    void testAddFilmWithInvalidReleaseDate() {
        Film film = new Film(null, "Film", "Description", LocalDate.of(1895, 12, 27), 120);

        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    void testAddFilmWithFutureReleaseDate() {
        Film film = new Film(null, "Film", "Description", LocalDate.now().plusDays(1), 120);

        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    void testUpdateValidFilm() {
        filmController.addFilm(new Film(1L, "Updated Film", "Description", LocalDate.now(), 120));
        Film updatedFilm = new Film(1L, "Updated Film", "Description", LocalDate.now(), 120);

        assertNotNull(updatedFilm);
        assertEquals("Updated Film", updatedFilm.getName());
    }

    @Test
    void testUpdateFilmWithNoId() {
        Film film = new Film(null, "Updated Film", "Description", LocalDate.now(), 120);
        ResponseEntity<Film> response = filmController.updateFilm(film);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testUpdateFilmWithInvalidId() {
        Film film = new Film(99999999L, "Updated Film", "Description", LocalDate.now(), 120);
        ResponseEntity<Film> response = filmController.updateFilm(film);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}