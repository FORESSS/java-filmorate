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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.filmorate.dto.genre.GenreDTO;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class GenreControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private MockMvc mockMvc;
    @Mock
    private GenreService genreService;
    @InjectMocks
    private GenreController genreController;

    @BeforeEach
    void setMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(genreController).build();
    }

    @Test
    @DisplayName("GET /genres")
    void getAllGenresTest() throws Exception {
        Collection<GenreDTO> genreDTOList = Collections.singletonList(createGenreDTO());
        Mockito.when(genreService.getAllGenres()).thenReturn(genreDTOList);
        mockMvc.perform(get("/genres"))
                .andExpect(status().isOk());
        Mockito.verify(genreService).getAllGenres();
    }

    @Test
    @DisplayName("GET /genres/{genreId}")
    void getGenreByIdTest() throws Exception {
        int genreId = 1;
        GenreDTO genreDTO = createGenreDTO();
        Mockito.when(genreService.getGenreById(genreId)).thenReturn(genreDTO);
        mockMvc.perform(get("/genres/{genreId}", genreId))
                .andExpect(status().isOk());
        Mockito.verify(genreService).getGenreById(genreId);
    }

    private GenreDTO createGenreDTO() {
        return GenreDTO.builder()
                .id(1)
                .name("Action")
                .build();
    }
}