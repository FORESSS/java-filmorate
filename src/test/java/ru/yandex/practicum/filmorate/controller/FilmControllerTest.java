package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FilmControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;
    private Film film;

    @BeforeEach
    public void create() {
        film = new Film(1L, "Film", "Description",
                LocalDate.of(2024, 1, 1), 120);
    }

    @Test
    public void testGetAllFilms() {
        ResponseEntity<Film[]> response = restTemplate.getForEntity("/films", Film[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetFilmById() {
        restTemplate.postForEntity("/films", film, Film.class);
        ResponseEntity<Film> response = restTemplate.getForEntity("/films/1", Film.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testCreateFilm() {
        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testUpdateFilm() {
        film.setName("Updated Film");
        restTemplate.postForEntity("/films", film, Film.class);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Film> requestEntity = new HttpEntity<>(film, headers);
        ResponseEntity<Film> response = restTemplate.exchange("/films", HttpMethod.PUT, requestEntity, Film.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated Film", response.getBody().getName());
    }

    @Test
    public void testLikeFilm() {
        restTemplate.postForEntity("/films", film, Film.class);
        restTemplate.put("/films/1/like/1", null);
        ResponseEntity<Film> response = restTemplate.getForEntity("/films/1", Film.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testRemoveLike() {
        restTemplate.postForEntity("/films", film, Film.class);
        restTemplate.put("/films/1/like/1", null);
        restTemplate.delete("/films/1/like/1");
        ResponseEntity<Film> response = restTemplate.getForEntity("/films/1", Film.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().getLikes().contains(1L));
    }

    @Test
    public void testGetPopularFilms() {
        restTemplate.postForEntity("/films", film, Film.class);
        ResponseEntity<Film[]> response = restTemplate.getForEntity("/films/popular?count=1", Film[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
    }

    @Test
    public void testCreateFilmWithBlankName() {
        film.setName("");
        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testUpdateFilmWitNullId() {
        film.setId(null);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Film> requestEntity = new HttpEntity<>(film, headers);
        ResponseEntity<Film> response = restTemplate.exchange("/films", HttpMethod.PUT, requestEntity, Film.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testUpdateFilmWithInvalidId() {
        film.setId(999L);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Film> requestEntity = new HttpEntity<>(film, headers);
        ResponseEntity<Film> response = restTemplate.exchange("/films", HttpMethod.PUT, requestEntity, Film.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAddFilmWithLongDescription() {
        StringBuilder longDescriptionBuilder = new StringBuilder();
        for (int i = 0; i < 201; i++) {
            longDescriptionBuilder.append("a");
        }
        film.setDescription(longDescriptionBuilder.toString());
        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testAddFilmWithNegativeDuration() {
        film.setDuration(-120);
        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testAddFilmWithFutureReleaseDate() {
        film.setReleaseDate(LocalDate.now().plusMonths(1));
        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testAddFilmWithInvalidReleaseDate() {
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}