package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.filmorate.dto.film.FilmDTO;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequestDTO;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequestDTO;
import ru.yandex.practicum.filmorate.dto.genre.GenreDTO;
import ru.yandex.practicum.filmorate.dto.genre.GenreRequestDTO;
import ru.yandex.practicum.filmorate.dto.rating.RatingDTO;
import ru.yandex.practicum.filmorate.dto.rating.RatingRequestDTO;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class FilmControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private MockMvc mockMvc;
    @Mock
    private FilmService filmService;
    @InjectMocks
    private FilmController filmController;

    @BeforeEach
    void setMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(filmController).build();
    }

    @Test
    @DisplayName("GET /films")
    void getAllFilmsTest() throws Exception {
        List<FilmDTO> filmDTOList = Collections.singletonList(createFilmDTO());
        Mockito.when(filmService.getAllFilms()).thenReturn(filmDTOList);
        mockMvc.perform(get("/films"))
                .andExpect(status().isOk());
        Mockito.verify(filmService).getAllFilms();
    }

    @Test
    @DisplayName("GET /films/{id}")
    void getFilmByIdTest() throws Exception {
        int filmId = 1;
        FilmDTO filmDTO = createFilmDTO();
        Mockito.when(filmService.getFilmById(filmId)).thenReturn(filmDTO);
        mockMvc.perform(get("/films/{id}", filmId))
                .andExpect(status().isOk());
        Mockito.verify(filmService).getFilmById(filmId);
    }

    @Test
    @DisplayName("POST /films")
    void addFilmTest() throws Exception {
        NewFilmRequestDTO dto = NewFilmRequestDTO.builder()
                .name("Фильм")
                .description("Описание")
                .releaseDate(LocalDate.of(2023, 1, 1))
                .duration(120)
                .mpa(RatingRequestDTO.builder().id(1).build())
                .build();
        String json = objectMapper.writeValueAsString(dto);
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
        Mockito.verify(filmService).addFilm(any(NewFilmRequestDTO.class));
    }

    @Test
    @DisplayName("PUT /films")
    void updateFilmTest() throws Exception {
        UpdateFilmRequestDTO dto = UpdateFilmRequestDTO.builder()
                .id(1)
                .name("Новый фильм")
                .description("Новое описание")
                .releaseDate(LocalDate.of(2023, 1, 1))
                .duration(120)
                .mpa(RatingRequestDTO.builder().id(1).build())
                .genres(Collections.singletonList(GenreRequestDTO.builder().id(1).build()))
                .build();
        String json = objectMapper.writeValueAsString(dto);
        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
        Mockito.verify(filmService).updateFilm(any(UpdateFilmRequestDTO.class));
    }

    @Test
    @DisplayName("PUT /films/{id}/like/{userId}")
    void addLikeToFilmTest() throws Exception {
        int filmId = 1;
        int userId = 1;
        mockMvc.perform(put("/films/{id}/like/{userId}", filmId, userId))
                .andExpect(status().isOk());
        Mockito.verify(filmService).addLikeToFilm(eq(filmId), eq(userId));
    }

    @Test
    @DisplayName("DELETE /films/{id}/like/{userId}")
    void deleteLikeFromFilmTest() throws Exception {
        int filmId = 1;
        int userId = 1;
        mockMvc.perform(delete("/films/{id}/like/{userId}", filmId, userId))
                .andExpect(status().isOk());
        Mockito.verify(filmService).deleteLikeFromFilm(eq(filmId), eq(userId));
    }

    @Test
    @DisplayName("GET /films/popular")
    void getMostPopularFilmsTest() throws Exception {
        int count = 10;
        List<FilmDTO> filmDTOList = Collections.singletonList(createFilmDTO());
        Mockito.when(filmService.getMostPopularFilms(count)).thenReturn(filmDTOList);
        mockMvc.perform(get("/films/popular")
                        .param("count", String.valueOf(count)))
                .andExpect(status().isOk());
        Mockito.verify(filmService).getMostPopularFilms(count);
    }

    @Test
    @DisplayName("GET /films/{id}/like")
    void getFilmLikesTest() throws Exception {
        int filmId = 1;
        int likesCount = 100;
        Mockito.when(filmService.getFilmLikes(filmId)).thenReturn(likesCount);
        mockMvc.perform(get("/films/{id}/like", filmId))
                .andExpect(status().isOk());
        Mockito.verify(filmService).getFilmLikes(filmId);
    }

    private FilmDTO createFilmDTO() {
        return FilmDTO.builder()
                .id(1)
                .name("Фильм")
                .description("Описание")
                .releaseDate(LocalDate.of(2023, 1, 1))
                .duration(120)
                .mpa(RatingDTO.builder().id(1).build())
                .genres(Collections.singletonList(GenreDTO.builder().id(1).build()))
                .build();
    }
}