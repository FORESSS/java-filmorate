package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private FilmController filmController;

    @BeforeEach
    void setUp() {
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
        Film addedFilm = filmController.addFilm(film);
        assertNotNull(addedFilm.getId());
        assertEquals("Film", addedFilm.getName());
        assertEquals("Description", addedFilm.getDescription());
        assertEquals(LocalDate.now(), addedFilm.getReleaseDate());
        assertEquals(120, addedFilm.getDuration());
    }

    @Test
    void testAddFilmWithBlankName() {
        Film film = new Film(null, "", "Description", LocalDate.now(), 120);
        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    void testAddFilmWithLongDescription() {
        StringBuilder longDescription = new StringBuilder();
        for (int i = 0; i < 201; i++) {
            longDescription.append("a");
        }
        Film film = new Film(null, "Test Film", longDescription.toString(), LocalDate.now(), 120);
        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    void testAddFilmWithFutureReleaseDate() {
        Film film = new Film(null, "Test Film", "Description", LocalDate.now().plusDays(1), 120);
        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    void testAddFilmWithNegativeDuration() {
        Film film = new Film(null, "Test Film", "Description", LocalDate.now(), -120);
        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    void testUpdateValidFilm() {
        Film film = new Film(123L, "Updated Film", "Updated Description", LocalDate.of(2024, 1, 1), 120);
        Film updatedFilm = filmController.updateFilm(film);
        assertEquals(123L, updatedFilm.getId());
        assertEquals("Updated Film", updatedFilm.getName());
        assertEquals("Updated Description", updatedFilm.getDescription());
    }

    @Test
    void testUpdateFilmWithNoId() {
        Film film = new Film(null, "Updated Film", "Updated Description", LocalDate.now(), 120);
        assertThrows(ValidationException.class, () -> filmController.updateFilm(film));
    }
}