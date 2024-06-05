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
    public void setUp() {
        film = new Film(1L, "Test Film", "Description", LocalDate.of(2000, 1, 1), 120, Collections.emptySet());
    }

    @Test
    public void getAllFilms() {
        ResponseEntity<Film[]> response = restTemplate.getForEntity("/films", Film[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void getFilmById() {
        restTemplate.postForEntity("/films", film, Film.class);
        ResponseEntity<Film> response = restTemplate.getForEntity("/films/1", Film.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void createFilm() {
        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void updateFilm() {
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
    public void likeFilm() {
        restTemplate.postForEntity("/films", film, Film.class);
        restTemplate.put("/films/1/like/1", null);
        ResponseEntity<Film> response = restTemplate.getForEntity("/films/1", Film.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getLikes().contains(1L));
    }

    @Test
    public void removeLike() {
        restTemplate.postForEntity("/films", film, Film.class);
        restTemplate.put("/films/1/like/1", null);
        restTemplate.delete("/films/1/like/1");
        ResponseEntity<Film> response = restTemplate.getForEntity("/films/1", Film.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().getLikes().contains(1L));
    }

    @Test
    public void getPopularFilms() {
        restTemplate.postForEntity("/films", film, Film.class);
        ResponseEntity<Film[]> response = restTemplate.getForEntity("/films/popular?count=1", Film[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
    }
}